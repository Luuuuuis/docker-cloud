package de.luuuuuis.cloud.docker.services;

import com.github.dockerjava.api.model.*;
import de.luuuuuis.cloud.docker.Docker;
import io.netty.channel.Channel;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class Service {

    private final Docker docker;
    private final ResourceRequirements resourceRequirements = new ResourceRequirements().withLimits(new ResourceSpecs()
            .withNanoCPUs((long) (1.5 * 10_0000 * 10_000)) // equivalent of 0.5 CPUs
            .withMemoryBytes(2147483648L)); // 2GB

    @Getter
    private int replicas = 0;
    public HashMap<String, Channel> channels = new HashMap<>();

    public Service(Docker docker, List<Service> serviceList) {
        this.docker = docker;
        addShutdownHook();

        serviceList.add(this);
        createService();
    }

    public abstract ServicePlacement servicePlacement();

    public abstract List<PortConfig> ports();

    public abstract EndpointResolutionMode endpointResolutionMode();

    public abstract ContainerSpec containerSpec();

    public abstract String serviceName();

    private void createService() {
        docker.getDockerClient()
                .createServiceCmd(getServiceSpec(serviceName(), new TaskSpec().withContainerSpec(containerSpec()).withResources(resourceRequirements), 0))
                .exec();

        System.out.println("Created Service " + serviceName());
        updateService(1);
    }

    private void addShutdownHook() {
        Thread shutdownThread = new Thread(() -> {
            com.github.dockerjava.api.model.Service service = docker.getDockerClient().listServicesCmd()
                    .withNameFilter(Collections.singletonList(serviceName())).exec().get(0);
            docker.getDockerClient().removeServiceCmd(service.getId()).exec();
            System.out.println("Removed Service: " + serviceName());
        }, "shutdown/" + serviceName());
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    public void updateService(int replicas) {
        com.github.dockerjava.api.model.Service service = getService();

        ServiceSpec serviceSpec = getServiceSpec(serviceName(),
                new TaskSpec()
                        .withContainerSpec(containerSpec())
                        .withPlacement(servicePlacement())
                        .withRestartPolicy(new ServiceRestartPolicy().withCondition(ServiceRestartCondition.ANY))
                        .withResources(resourceRequirements), replicas);
        docker.getDockerClient().updateServiceCmd(service.getId(), serviceSpec)
                .withVersion(Objects.requireNonNull(service.getVersion()).getIndex()).exec();

        System.out.println("Scaled service " + serviceName() + " to " + replicas);
        this.replicas = replicas;
    }

    private ServiceSpec getServiceSpec(String name, TaskSpec taskSpec, int replicas) {
        return new ServiceSpec()
                .withName(name)
                .withTaskTemplate(taskSpec)
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(replicas)))
                .withEndpointSpec(new EndpointSpec().withPorts(ports()).withMode(endpointResolutionMode()))
                .withNetworks(Collections.singletonList(new NetworkAttachmentConfig().withTarget("r6network")));
    }

    public com.github.dockerjava.api.model.Service getService() {
        return docker.getDockerClient().listServicesCmd().withNameFilter(Collections.singletonList(serviceName())).exec().get(0);
    }
}
