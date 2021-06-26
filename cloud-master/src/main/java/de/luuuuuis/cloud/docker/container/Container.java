package de.luuuuuis.cloud.docker.container;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import de.luuuuuis.cloud.docker.Docker;
import io.netty.channel.Channel;

import java.util.Collections;
import java.util.List;

public abstract class Container {

    private final Docker docker;
    public String id;
    public Channel channel;

    public Container(Docker docker, List<Container> containerList) {
        this.docker = docker;

        containerList.add(this);
        addShutdownHook();
        start();
    }

    public abstract List<PortBinding> portBindings();

    public abstract String containerName();

    public abstract String imageName();

    public abstract List<String> envVars();

    public abstract List<Bind> binds();

    private void addShutdownHook() {
        Thread shutdownThread = new Thread(() -> {
            com.github.dockerjava.api.model.Container container = docker.getDockerClient().listContainersCmd().withNameFilter(Collections.singletonList(containerName())).exec().get(0);
            docker.getDockerClient().stopContainerCmd(container.getId()).exec();
            docker.getDockerClient().removeContainerCmd(container.getId()).exec();
            System.out.println("Removed Container: " + containerName());
        }, "shutdown/" + containerName());
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    public void start() {
        CreateContainerResponse response = docker.getDockerClient().createContainerCmd(imageName())
                .withName(containerName())
                .withHostConfig(HostConfig.newHostConfig().withNetworkMode("r6network"))
                .withBinds(binds())
                .withPortBindings(portBindings())
                .withEnv(envVars())
                .withMemory(2147483648L)
                .exec();

        docker.getDockerClient().startContainerCmd(response.getId()).exec();
        id = response.getId();
        System.out.println("Started container " + id);
    }


}
