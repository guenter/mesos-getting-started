package mesosphere.mesos_scala

import org.apache.mesos.{SchedulerDriver, Scheduler}
import org.apache.mesos.Protos._
import java.util
import java.util.logging.Logger

/**
 * A Mesos framework scheduler that runs a given number of instances of a command across a cluster.
 *
 * @author Tobi Knaup
 */

class ExampleScheduler(command: String, numInstances: Int) extends Scheduler {

  val log = Logger.getLogger(getClass.getName)
  // Keep track of how many instances are currently running
  var currentInstances = 0

  def registered(driver: SchedulerDriver, p2: FrameworkID, p3: MasterInfo) {}

  def reregistered(driver: SchedulerDriver, p2: MasterInfo) {}

  def resourceOffers(driver: SchedulerDriver, offers: util.List[Offer]) {}

  def offerRescinded(driver: SchedulerDriver, p2: OfferID) {}

  def statusUpdate(driver: SchedulerDriver, status: TaskStatus) {}

  def frameworkMessage(driver: SchedulerDriver, executor: ExecutorID, slave: SlaveID, p4: Array[Byte]) {}

  def disconnected(driver: SchedulerDriver) {}

  def slaveLost(driver: SchedulerDriver, slave: SlaveID) {}

  def executorLost(driver: SchedulerDriver, executor: ExecutorID, slave: SlaveID, p4: Int) {}

  def error(driver: SchedulerDriver, error: String) {}
}
