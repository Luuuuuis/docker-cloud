package de.luuuuuis.cloud.docker.services.impl;

import com.github.dockerjava.api.model.*;
import de.luuuuuis.cloud.docker.services.Service;
import de.luuuuuis.cloud.docker.Docker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProxyService extends de.luuuuuis.cloud.docker.services.Service {

    public ProxyService(Docker docker, List<Service> serviceList) {
        super(docker, serviceList);
    }

    @Override
    public ServicePlacement servicePlacement() {
        return null;
    }

    @Override
    public List<PortConfig> ports() {
        return Collections.singletonList(new PortConfig().withPublishedPort(25565).withTargetPort(25577).withPublishMode(PortConfig.PublishMode.ingress));
    }

    @Override
    public EndpointResolutionMode endpointResolutionMode() {
        return null;
    }

    @Override
    public ContainerSpec containerSpec() {
        return new ContainerSpec()
                .withImage("bungeecord")
                .withMounts(Collections.singletonList(
                        new Mount().withSource("/home/luis/cloud/plugins/").withTarget("/plugins")))
                .withEnv(Collections.singletonList("GROUP=proxy"))
                .withDnsConfig(new ContainerDNSConfig().withNameservers(Arrays.asList("127.0.0.11:53", "1.1.1.1")));
    }

    @Override
    public String serviceName() {
        return "BungeeCord";
    }
}
