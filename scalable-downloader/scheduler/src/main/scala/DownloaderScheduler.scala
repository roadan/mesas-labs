import java.util
import java.util.concurrent.ConcurrentLinkedDeque

import org.apache.mesos.Protos._
import org.apache.mesos.{Scheduler, SchedulerDriver}

import scala.collection.JavaConversions._

/**
  * * Created by roadan on 7/23/16.
  **/
class DownloaderScheduler extends Scheduler {

  private var frameworkId: FrameworkID = null
  private val fileList = new ConcurrentLinkedDeque[String]()

  override def offerRescinded(schedulerDriver: SchedulerDriver, offerID: OfferID): Unit = ???

  override def disconnected(schedulerDriver: SchedulerDriver): Unit = ???

  override def reregistered(schedulerDriver: SchedulerDriver, masterInfo: MasterInfo): Unit = ???

  override def slaveLost(schedulerDriver: SchedulerDriver, slaveID: SlaveID): Unit = ???

  override def error(schedulerDriver: SchedulerDriver, s: String): Unit = ???

  override def statusUpdate(schedulerDriver: SchedulerDriver, taskStatus: TaskStatus): Unit = ???

  override def frameworkMessage(schedulerDriver: SchedulerDriver, executorID: ExecutorID, slaveID: SlaveID, bytes: Array[Byte]): Unit = ???

  override def resourceOffers(schedulerDriver: SchedulerDriver, list: util.List[Offer]): Unit = {

    for (offer <- list) {
      println(offer)
      schedulerDriver.declineOffer(offer.getId)
    }

  }

  override def registered(schedulerDriver: SchedulerDriver, frameworkID: FrameworkID, masterInfo: MasterInfo): Unit = {

    println("registered")
    this.frameworkId = frameworkId

  }

  def validateOffer(offer: Offer): Boolean = {

    val resources = offer.getResourcesList

    resources.count(r => r.getName == "cpus" && r.getScalar.getValue >= 0.1) > 0 &&
      resources.count(r => r.getName == "mem" && r.getScalar.getValue >= 128) > 0
  }

  def createScalarResource(name: String, value: Double): Resource = {
    Resource.newBuilder
      .setName(name)
      .setType(Value.Type.SCALAR)
      .setScalar(Value.Scalar.newBuilder().setValue(value)).build()
  }

  override def executorLost(schedulerDriver: SchedulerDriver, executorID: ExecutorID, slaveID: SlaveID, i: Int): Unit = ???

}

object DownloaderScheduler {

  def apply(): DownloaderScheduler = {

    val result = new DownloaderScheduler()

    result.fileList.add("IMG_6052.m4v")
    result.fileList.add("IMG_6051.m4v")
    result.fileList.add("IMG_6041.m4v")
    result.fileList.add("IMG_2228.m4v")

    result

  }

}
