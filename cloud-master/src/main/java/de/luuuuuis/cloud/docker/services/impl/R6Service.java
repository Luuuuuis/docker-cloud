package de.luuuuuis.cloud.docker.services.impl;

import com.github.dockerjava.api.model.*;
import de.luuuuuis.cloud.docker.services.Service;
import de.luuuuuis.cloud.docker.Docker;

import java.util.Collections;
import java.util.List;

public class R6Service extends de.luuuuuis.cloud.docker.services.Service {

    public R6Service(Docker docker, List<Service> serviceList) {
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
                .withMounts(Collections.singletonList(
                        new Mount().withSource("/home/luis/cloud/plugins/").withTarget("/plugins")))
                .withEnv(Collections.singletonList("GROUP=r6Server"));
    }

    @Override
    public String serviceName() {
        return "r6";
    }

}
