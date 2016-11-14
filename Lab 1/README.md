#Lab 1 - Installing and Configuring Docker

## Overview

In this lab we will install Docker on our Mesos node and will configure Mesos to support Docker in addition to Mesos ontainerizers

### TRask 1 - Try (and fail) Running a Docker Image

- Start the Vagrant machine and ssh into it using the following comands:

```
cd mesos-labs/vagrant
vagrant up
```
**Note:** All future labs in the workshop assume you have left the vagrant machine running.

- Open a web browser and open the mesos master UI
```
http://192.168.33.112:5050/
```
- In the top navigation bar select **Frameworks** and then click on the framework id next to **Marathon** to open the the Marathon Framwork Page and Click on the link next to **Web UI**
- In the Marthon UI, Select Create Application, switch to **JSON Mode** and paste in the following JSON:

```json
{
  "id": "123",
  "cmd": null,
  "cpus": 1,
  "mem": 128,
  "disk": 0,
  "instances": 1,
  "container": {
    "docker": {
      "image": "roadan/nodeapp",
      "network": "HOST",
      "forcePullImage": true,
      "portMappings": [
            { "hostPort": 0, "containerPort": 8181 } ]
    },
    "type": "DOCKER"
  }
}
```
- When your application will fail to create,

### Task 2 - Install Docker and Configure Mesos to use Docker

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

- repeat Task 1