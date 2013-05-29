package mesosphere.mesos_scala

import org.apache.mesos.MesosSchedulerDriver
import org.apache.mesos.Protos._
import org.apache.mesos.state.ZooKeeperState
import java.util.concurrent.TimeUnit

/**
 * @author Tobi Knaup
 */

object Main extends App {
  // Every Mesos framework needs a name
  val frameworkName = "mesos_scala-0.0.1"
  // URL of the Mesos master
  val master = args(0)
  // The command to run
  val command = args(1)
  // Number of instances to run
  val numInstances = args(2).toInt

  // FrameworkID uniquely identifies a framework and can be used for failover
  val frameworkId = FrameworkID.newBuilder
    .setValue(frameworkName)
    .build
  // FrameworkInfo describes a framework
  val frameworkInfo = FrameworkInfo.newBuilder
    .setName(frameworkName)
    .setId(frameworkId)
    .setUser("") // Let Mesos assign the user
    .setFailoverTimeout(60.0) // Allow a 60 second window for failover
    .build

  // Create a state object backed by ZK
  val state = new ZooKeeperState("localhost:2181", 10, TimeUnit.SECONDS, "/getting-started-state")
  // Create the scheduler, the driver, and run it
  val scheduler = new ExampleScheduler(command, numInstances, state)
  val driver = new MesosSchedulerDriver(scheduler, frameworkInfo, master)
  driver.run()
}