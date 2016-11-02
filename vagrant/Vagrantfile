# Vagrantfile for amaterasu dev station
Vagrant.configure(2) do |config|

  config.vm.box = "bento/centos-7.1"
  config.vm.network "private_network", ip: "192.168.33.112"
  config.vm.hostname = "demo-node"
  config.vm.synced_folder "~/Work/Sessions/dallas-buyers-framework/target/scala-2.11", "/dallas"
  config.vm.synced_folder "movies/", "/movies"
           
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
		vb.cpus = 2
  end

  config.vm.provision "shell", run: "always" do |s|
	  s.path = "provision.sh"
	  s.privileged = true 
  end		 
end
