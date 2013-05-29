package mesosphere.mesos_scala

import org.apache.mesos.{SchedulerDriver, Scheduler}
import org.apache.mesos.Protos._
import java.util
import java.util.logging.Logger
import org.apache.mesos.state.State

/**
 * A Mesos framework scheduler that runs a given number of instances of a command across a cluster.
 *
 * @author Tobi Knaup
 */

class ExampleScheduler(command: String, numInstances: Int, state: State) extends Scheduler {

  val log = Logger.getLogger(getClass.getName)
  val currentInstancesStateName = "currentInstances"
  // Keep track of how many instances are currently running
  var currentInstances = fetchCurrentInstances()

  def registered(driver: SchedulerDriver, p2: FrameworkID, p3: MasterInfo) {}

  def reregistered(driver: SchedulerDriver, p2: MasterInfo) {}

  def resourceOffers(driver: SchedulerDriver, offers: util.List[Offer]) {
    import scala.collection.JavaConversions._

    for (offer <- offers) {
      log.info("Received offer " + offer)

      if (currentInstances < numInstances) {
        val taskId = TaskID.newBuilder
          .setValue("task_" + System.currentTimeMillis())

        val cpuResource = Resource.newBuilder
          .setName("cpus")
          .setType(Value.Type.SCALAR)
          .setScalar(Value.Scalar.newBuilder().setValue(1))

        val commandInfo = CommandInfo.newBuilder
          .setValue(command)

        val task = TaskInfo.newBuilder
          .setName(taskId.getValue)
          .setTaskId(taskId)
          .setSlaveId(offer.getSlaveId)
          .addResources(cpuResource)
          .setCommand(commandInfo)
          .build

        log.info("Launching task " + taskId.getValue)
        driver.launchTasks(offer.getId, List(task))
        currentInstances += 1
        storeCurrentInstances()
      } else {
        log.info("Declining offer")
        driver.declineOffer(offer.getId)
      }
    }
  }

  def offerRescinded(driver: SchedulerDriver, p2: OfferID) {}

  def statusUpdate(driver: SchedulerDriver, status: TaskStatus) {
    log.info("Received status update " + status)

    if (status.getState.eq(TaskState.TASK_FAILED)) {
      currentInstances -= 1
      storeCurrentInstances()
    }
  }

  def frameworkMessage(driver: SchedulerDriver, executor: ExecutorID, slave: SlaveID, p4: Array[Byte]) {}

  def disconnected(driver: SchedulerDriver) {}

  def slaveLost(driver: SchedulerDriver, slave: SlaveID) {}

  def executorLost(driver: SchedulerDriver, executor: ExecutorID, slave: SlaveID, p4: Int) {}

  def error(driver: SchedulerDriver, error: String) {}

  private def fetchCurrentInstances(): Int = {
    val bytes = state.fetch(currentInstancesStateName).get.value

    val count = if (bytes.length > 0) {
      bytes(0).toInt
    } else {
      0
    }

    log.info("Fetched current instance count " + count)
    count
  }

  private def storeCurrentInstances(): Unit = {
    val oldVar = state.fetch(currentInstancesStateName).get
    val data = Array(currentInstances.toByte)
    val newVar = state.store(oldVar.mutate(data))

    if (data.sameElements(newVar.get.value)) {
      log.info("Successfully stored current instance count " + currentInstances)
    } else {
      log.warning("Failed to store current instance count")
    }
  }
}
