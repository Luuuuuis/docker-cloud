package de.luuuuuis.cloud.docker.services.impl;

import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.EndpointResolutionMode;
import com.github.dockerjava.api.model.PortConfig;
import com.github.dockerjava.api.model.ServicePlacement;
import de.luuuuuis.cloud.docker.Docker;
import de.luuuuuis.cloud.docker.services.Service;

import java.util.Collections;
import java.util.List;

public class LobbyService extends Service {

    public LobbyService(Docker docker, List<Service> serviceList) {
        super(docker, serviceList);
    }

    @Override
    public ServicePlacement servicePlacement() {
        return null;
    }

    @Override
    public List<PortConfig> ports() {
        return Collections.emptyList();
    }

    @Override
    public EndpointResolutionMode endpointResolutionMode() {
        return EndpointResolutionMode.DNSRR;
    }

    @Override
    public ContainerSpec containerSpec() {
        return new ContainerSpec()
                .withImage("spigot")
                .withEnv(Collections.singletonList("GROUP=Lobby"));
    }

    @Override
    public String serviceName() {
        return "Lobby";
    }
}
