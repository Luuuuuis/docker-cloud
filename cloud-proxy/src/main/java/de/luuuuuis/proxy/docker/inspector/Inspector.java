package de.luuuuuis.proxy.docker.inspector;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.command.EventsResultCallback;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Inspector {

    ProxyServer proxyServer;
    DockerClient dockerClient;

    public Inspector(ProxyServer proxyServer, DockerClient dockerClient) {
        this.proxyServer = proxyServer;
        this.dockerClient = dockerClient;
    }

    public void runContainerInspection() {
        EventsResultCallback callback = this.getEventResultCallback();
        List<Container> containers = this.dockerClient.listContainersCmd()
                .withNetworkFilter(Collections.singletonList("r6network"))
                .exec();

        // Trigger fake Event to use same Result Callback
        containers.forEach(container -> {
            // filter it self out
            if (Arrays.asList(container.getNames()).contains("/BungeeCord"))
                return;

            // inspecting container to get already running containers at bungee startup
            InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

            if (inspectContainerResponse.getState().getHealth().getStatus().equals("healthy")) {
                Event event = new Event("running", container.getId(), container.getImage(), System.currentTimeMillis())
                        .withAction("health_status: healthy")
                        .withType(EventType.forValue("container"));

                callback.onNext(event);
            }
        });
    }

    @SneakyThrows
    public void runContainerListener() {
        this.dockerClient.eventsCmd().exec(this.getEventResultCallback()).awaitCompletion().close();
    }

    private EventsResultCallback getEventResultCallback() {
        return new ResultCallback(
                this.dockerClient,
                this.proxyServer
        );
    }
}
