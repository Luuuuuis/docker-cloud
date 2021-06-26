package de.luuuuuis.proxy.docker.inspector;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.EventsResultCallback;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class ResultCallback extends EventsResultCallback {

    DockerClient dockerClient;
    ProxyServer proxyServer;

    public ResultCallback(DockerClient dockerClient, ProxyServer proxyServer) {
        this.dockerClient = dockerClient;
        this.proxyServer = proxyServer;
    }

    @Override
    public void onNext(Event event) {
        if(event.getType() == null || !event.getType().getValue().equals("container")) {
            super.onNext(event);
            return;
        }

        ContainerEvent containerEvent = new ContainerEvent(event.getId(), event.getAction());

        try {
            InspectContainerResponse info = this.dockerClient.inspectContainerCmd(event.getId()).exec();

            containerEvent.setName(info.getName());
            containerEvent.setEnvVars(this.getEnvironmentVariables(info));
            containerEvent.setPort(this.getPort(info));
            containerEvent.setIp(this.getIp(info));

        } catch (NotFoundException e) {
            super.onNext(event);
            return;
        }

        super.onNext(event);
        this.proxyServer.getPluginManager().callEvent(containerEvent);
    }

    private Integer getPort(InspectContainerResponse info) {
        Map<ExposedPort, Ports.Binding[]> portBindings = info.getNetworkSettings().getPorts().getBindings();

        if (portBindings.keySet().size() > 0 && portBindings.keySet().iterator().next().getPort() != 0) {
            return portBindings.keySet().iterator().next().getPort();
        }

        return null;
    }

    private InetAddress getIp(InspectContainerResponse info) {
        if (!info.getNetworkSettings().getNetworks().containsKey("r6network")) {
            return null;
        }

        try {
            return InetAddress.getByName(info.getNetworkSettings().getNetworks().get("r6network").getIpAddress());
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private Map<String, String> getEnvironmentVariables(InspectContainerResponse info) {
        String[] unformattedArray =  info.getConfig().getEnv();

        if(unformattedArray == null || unformattedArray.length == 0) {
            return  new HashMap<>(0);
        }

        Map<String, String> formattedMap = new HashMap<>(unformattedArray.length);

        for (String environmentVariable: unformattedArray) {
            String[] parts = environmentVariable.split("=");
            if (parts.length == 2) {
                formattedMap.put(parts[0], parts[1]);
            }
        }
        return formattedMap;
    }
}
