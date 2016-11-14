import org.apache.mesos.{ MesosExecutorDriver, ExecutorDriver }

/**
  * Created by roadan on 8/2/16.
  */
object ExecutorLauncher extends App {

  val executor = DownloaderExecutor()
  val driver = new MesosExecutorDriver(executor)
  driver.run()

}
