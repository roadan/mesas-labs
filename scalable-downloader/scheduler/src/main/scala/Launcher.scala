import org.apache.mesos.{ MesosSchedulerDriver, Protos }

/**
  * Created by roadan on 7/23/16.
  */
object Launcher extends App {

  val frameworkBuilder = Protos.FrameworkInfo.newBuilder()
    .setName(s"The scalable downloader framework")
    .setFailoverTimeout(10)
    .setUser("root")

  val scheduler = DownloaderScheduler()

  val driver = new MesosSchedulerDriver(scheduler, frameworkBuilder.build(), "192.168.33.112:5050")
  driver.run()

}