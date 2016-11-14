#Lab 2 - Building a scheduler

## Overview

In this lab we build a simple, scheduler only framework. Our framework will create a scalable downloader, that receives a list of files to download, and downloads those in parallel across the cluster. **Note:** The first step is optional, but it will give you a good indication whether your environment is configured correctly, and is highly recommended.
### Task 1 (optional) - Run the Empty Schaduler, and Print the resource offers

- Change the directory to the `mesos-labs/scalable-downloader` directory and build the *scalable-downloader* project using the following commands:

```
cd mesos-labs/scalable-downloader
gradle shadowJar
```

- In the vagrnt machine run the following line to launch the empty scheduler.

```
java -cp /scheduler/scheduler-all.jar -Djava.library.path=/usr/lib Launcher
```
- stop the framework from running but pressing `ctrl+c` and running the following lines:

```
sudo service mesos-slave restart
sudo service mesos-master restart
```

### Task 2 - Implement the Scheduler Logic

In this step we are going to implement the `resourceOffers` method of the `DownloaderScheduler` class.

- Open the `DownloaderScheduler` class and review the `validateOffer` method. This method examines the resources available in a specific offer, and validate those are sufficient to perform a task. In this example we have set the required threshold to 0.1 CPUs and 12m MB of RAM.- Navigate to the the `resourceOffers` method and replace the implementation inside the for loop to validate each and every resource offer using the following code:
```Scala
 if (!validateOffer(offer)) {

    // please do this!!!
    schedulerDriver.declineOffer(offer.getId)

 }
```

**Note:** In order to make the resource offers available as soon as possible to other frameworks, it is recommended to decline offers who are not suitable for your needs. Be kind to other frameworks on the cluster and do that.- Next we are going to implement the logic to handle valid offers. Add an `else` statement to the `if` statement we have created in the previous task.

```
No code to paste, I'm sure you can add an else statment on your own :)
```

**Note:** the rest of the instructions in *Task 2* are to be implemented inside the `else` block.

- Now that we have an offer that can execute our task, we can check if there are any files waiting to be downloaded. The `downloadQueue` member of the `DownloaderScheduler` class is a ` ConcurrentLinkedDeque[String]` containing the URIs of the files to be downloaded.  Use the following code to check if there is additional work in the queue and if not, terminate the framework:

```
// in some scenarios we might decline the offer and
// continue running the scheduler
if (downloadQueue.isEmpty()) {

  schedulerDriver.declineOffer(offer.getId)
  schedulerDriver.stop()
  System.exit(0)
}
```
- Next we are going to create a new instance if the CommandInfo protobuf  that describes the command to be executed by Mesos and set our command to use `wget` to download a file

```
//lets download this movie
val file = downloadQueue.pop()

val commandInfo = CommandInfo.newBuilder
  .setValue(s"wget $file")
```

**Note:** Because we are not creating an ExecutorInfo protobuf, Mesos will use the command executor to execute our task. You can read more about the command executor in the [Framework Development Guide](http://mesos.apache.org/documentation/latest/app-framework-development-guide/)- Next we are going to create the TaskInfo protobuf, that configures the task to be run by the executor. Every task must have a unique id, a command, and potentially an executor (will be detailed in the next lab). In addition a task has resources associated with it. Create the TaskID and TaskInfo protobufs using the following code:

```
val taskId = TaskID.newBuilder
  .setValue("task_" + System.currentTimeMillis())

val task = TaskInfo.newBuilder
  .setName(taskId.getValue)
  .setTaskId(taskId)
  .setSlaveId(offer.getSlaveId)
  .addResources(createScalarResource("cpus", 0.2))
  .addResources(createScalarResource("mem", 128))
  .setCommand(commandInfo)
  .build

```

- The thing we need to do in the `resourceOffers` method is to use the schedulerDriver to launch our tasks (and also printout a nifty debug message).

```
println(s"|--> starting to download $file")
schedulerDriver.launchTasks(java.util.Collections.singleton(offer.getId), List(task))
```
- Built the scheduler using the following command:

```
gradle shadowJar
```

W00t!! our scheduler is now complete!!

### Step 3 - Running our Framework

- Before running our framework, we will need to serve the files to download. To do so we will use the Python SimpleHTTPServer module as follows:

```
cd /executor
nohup python -m SimpleHTTPServer 8000 &
cd /scheduler
```
- Now we can run newly built framework:

```
java -cp /scheduler/scheduler-all.jar -Djava.library.path=/usr/lib Launcher
```
Well done!