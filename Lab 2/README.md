#Lab 2 - Building a scheduler

## Overview

In this lab we buld a simple, scheduler only framework. In order to perform this lab, you will need to have the Dallas Buyers framework source code. If you do not have the source code available on your computer you can get it by cloning the GitHub repo [here](https://github.com/roadan/dallas-buyers-framework).

**Note:** The first step is optional, but it will give you a good indication whether your enviroment is configured correctly, and is highly recomanded.

### Step 1 (optional) - Run the Empty Schaduler, and Print the resource offers

- Change the directory to the `mesos-labs/scalable-downloader` directory and build the *scalable-downloader* project using the following comands:

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

### Step 2 - Implement the Scheduler Logic

- Install and start Docker on the Vagrant Machine

```
sudo yum install -y docker
sudo chkconfig docker on
sudo service docker start
```

- Configure Mesos to support both Docker and Mesos containerizers, by cerating the `/etc/mesos-slave/containerizers` file and adding the following line to it: `docker,mesos`

-  restart the Mesos slave deamon

```
sudo service mesos-slave restart
```

- repeat Step 1