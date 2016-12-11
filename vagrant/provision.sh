#!/usr/bin/env bash

echo "Setting up the hosts file"
echo "192.168.33.112 demo-node" >> /etc/hosts

echo "installin mesos and marathon"
echo "----------------------------"

rpm -Uvh http://repos.mesosphere.com/el/7/noarch/RPMS/mesosphere-el-repo-7-1.noarch.rpm
yum -y install mesos marathon
echo "IP=192.168.33.112" >> /etc/default/mesos
echo "192.168.33.112" >> /etc/mesos/ip
echo "192.168.33.112" >> /etc/mesos-master/hostname
#echo "192.168.33.10" >> /etc/mesos-slave/ip
#echo "192.168.33.10" >> /etc/mesos-slave/hostname

echo "export LIBPROCESS_IP=192.168.33.112" >> ~/.bashrc
echo "export SPARK_LOCAL_IP=192.168.33.112" >> ~/.bashrc

sourc ~/.bashrc
#setting the --no-switch_user flag for mesos slave
sudo chmod 777 -R /etc/mesos-slave/
touch /etc/mesos-slave/?no-switch_user

echo "installing zookeeper"
echo "--------------------"

rpm -Uvh http://archive.cloudera.com/cdh4/one-click-install/redhat/6/x86_64/cloudera-cdh-4-0.x86_64.rpm
sudo yum -y install zookeeper zookeeper-server

sudo -u zookeeper zookeeper-server-initialize --myid=1
sudo service zookeeper-server start

echo "Starting mesos"
echo "--------------"

service mesos-master start
service mesos-slave start
service marathon start

cd /executor/
#wget https://s3-ap-southeast-2.amazonaws.com/amaterasu/movies.zip
#unzip movies

