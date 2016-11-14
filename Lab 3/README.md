#Lab 3 - Building an Executor

## Overview

In this lab we extend our scalable downloader framework to contain a simple custom executor.

### Task 1 - Implement the Executor Logic

- Open the `DownloaderExecutor` class in the Executor module and locate the `launchTask` method. 
- Extract the download URI from the `taskInfo` protobuf using the following code:

```scala
// getting the URI
val uri = new String(taskInfo.getData.toByteArray)
```

- before we launch our task we need to notify Mesos that a task is starting using the following code:

```scala
val status = TaskStatus.newBuilder
  .setTaskId(taskInfo.getTaskId)
  .setState(TaskState.TASK_STARTING).build()

driver.sendStatusUpdate(status)
```
- Next, we  will use the Play! WS `NingWSClient` to download our file:

```scala
val wsClient = NingWSClient()
wsClient
  .url(uri)
  .get()
  .map { wsResponse => {

    }
  }
```

- In the previous step we have used the `NingWSClient` to issue an HTTP get request, but we still need to save it to disk ourselves. To do so, we will implement the `map` callback. The first step in our implementation would be to update the status of the task to running using the following code:

```scala
var status = TaskStatus.newBuilder
  .setTaskId(taskInfo.getTaskId)
  .setState(TaskState.TASK_RUNNING).build()

driver.sendStatusUpdate(status)
```
- Next we save the content of the response to file, and send a notification to our scheduler that the file was downloaded:

```scala
val file = new File("." + uri.substring(uri.lastIndexOf("/")))
file.createNewFile()
val fos = new FileOutputStream(file, false)
fos.write(wsResponse.bodyAsBytes)
fos.close()

driver.sendFrameworkMessage(s"movie $uri returned ${wsResponse.status}".getBytes())

```
**Note:** the framework message is a convenient mechanism to communicate between the Scheduler and Executors, but it is not a formal requirement  for implementing an Executor.

- Last, we update the status of our task to finished

```scala
status = TaskStatus.newBuilder
  .setTaskId(taskInfo.getTaskId)
  .setState(TASK_FINISHED).build()

driver.sendStatusUpdate(status)
```

### Task 2 - Change the Scheduler Logic

In this step we are going to change the implementation of the `resourceOffers` method of the `DownloaderScheduler` class to use our new executor.

- Open the `DownloaderScheduler` class and got to the `resourceOffers ` method. Find the line that crates the `CommandInfo` protobuf and replace it with the following code, which executes the
```scala
// now our command runs our executor
val commandInfo = CommandInfo.newBuilder
  .setValue(s"java -cp executor-all.jar ExecutorLauncher")
  .addUris(CommandInfo.URI.newBuilder
    .setValue("http://192.168.33.112:8000/executor-all.jar")
    .setCache(false) //this is for dev (you will need to redeploy the executor)
    .setExtract(false)
    .setExecutable(false)
    .build())
  .build()
```

**Note:** The addUris method allows us to add files to be downloaded as part of the container creation. This is useful for downloading the binaries for the executor and any dependencies we might need. 
Files downloaded by mesos can also be cached, extracted and executed before running the command.- Next, we will create an ExecutorInfo protobuf, which contains an executor id and the command info. 

```scala
// configuring our executor
val executorId = ExecutorID.newBuilder()
  .setValue(UUID.randomUUID.toString)
  .build()

val executorInfo = ExecutorInfo.newBuilder()
  .setExecutorId(executorId)
  .setFrameworkId(frameworkId)
  .setCommand(commandInfo)
  .build()
```

**Note:** when using the command executor, like we did in the previus lab, we pass the CommandInfo to the TaskInfo without using the executor info.

- Locate the TaskInfo protobuf object builder section and replace the `setCommand` method call with a call to the `setExecutor` method as following:

```scala
//.setCommand(commandInfo)
.setExecutor(executorInfo)
```

- Implement the `frameworkMessage` method to print framework messages sent by the executor, using the following code:

```scala
println("************* incoming message *************")
println(s"slave id: ${slaveID.getValue}")
println(s"executor id: ${executorID.getValue}")
println(new String(bytes))
println("********************************************")
```
- rebuild the framework

```
gradle shadowJar
```

### Step 3 - Running our Framework

- Now we can re-run our framework:

```
java -cp /scheduler/scheduler-all.jar -Djava.library.path=/usr/lib Launcher
```
Well done!