package de.luuuuuis.cloud.docker.container.impl;

import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Volume;
import de.luuuuuis.cloud.docker.container.Container;
import de.luuuuuis.cloud.docker.Docker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BCContainer extends Container {

    public BCContainer(Docker docker, List<Container> containerList) {
        super(docker, containerList);
    }

    @Override
    public List<PortBinding> portBindings() {
        return Arrays.asList(PortBinding.parse("25565:25577/udp"), PortBinding.parse("25565:25577/tcp"));
    }

    @Override
    public String containerName() {
        return "BungeeCord";
    }

    @Override
    public String imageName() {
        return "bungeecord";
    }

    @Override
    public List<String> envVars() {
        return Collections.emptyList();
    }

    @Override
    public List<Bind> binds() {
        return Arrays.asList(
                //new Bind("C:\\Users\\luist\\IdeaProjects\\MinecraftR6S\\proxy\\target\\", new Volume("/plugins")),
                new Bind("/home/luis/cloud/plugins/", new Volume("/plugins")),
                new Bind("/var/run/docker.sock", new Volume("/var/run/docker.sock")));
    }
}
