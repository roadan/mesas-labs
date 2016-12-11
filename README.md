#Mesos-Labs Dev Machine Vagrant Setup

This repository contains the vagrentfile and provisioning script for creating a dev machine for running the dallas framework demos.

##prerequisites

Before you create a dev machine, make sure you have the dallas-buyers-framework source code, to do so, simply clone the Amaterasu git repo:

```
git clone https://github.com/roadan/dallas-buyers-framework.git 
``` 

**Note:** the vagrentfile assumes you have cloned the repository in the  */dallas* directory. If you have cloned the repository to a different path, please edit the following line before continue the installation to point to the correct path, for example:


```
"~/Work/Sessions/dallas-buyers-framework/target/scala-2.11", "/dallas"

```

You should also map a folder containing files to dawnload under *movies/*

##Installation

Clone this repository to your local machine:

```
git clone https://github.com/roadan/mesos-labs.git
``` 
Change the directory to mesos-labs/vagrant and start the machine:

```
cd mesos-labs/vagrant
vagrant up
```

Ssh to the vagrant machine:

```
vagrant ssh
```
