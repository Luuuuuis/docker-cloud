package de.luuuuuis.cloud.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import de.luuuuuis.cloud.docker.container.Container;
import de.luuuuuis.cloud.docker.container.impl.BCContainer;
import de.luuuuuis.cloud.docker.services.Service;
import de.luuuuuis.cloud.docker.services.impl.LobbyService;
import de.luuuuuis.cloud.docker.services.impl.R6Service;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class Docker {

    @Getter
    private final DockerClient dockerClient;

    @Getter
    private final List<Service> serviceList = new ArrayList<>();

    @Getter
    private final List<Container> containerList = new ArrayList<>();

    @SneakyThrows
    public Docker() {
        DockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                //.withDockerHost("tcp://localhost:2375")
                //.withDockerHost("unix:///var/run/docker.sock")
                .withDockerHost("tcp://proxy.luis.team:2376")
                .withDockerTlsVerify(true)
                .withDockerCertPath("/home/luis/cloud/cloud/certs/")
                //.withDockerCertPath("C:\\Users\\luist\\IdeaProjects\\MinecraftR6S\\cloud-master\\src\\main\\resources")
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(clientConfig.getDockerHost())
                .sslConfig(clientConfig.getSSLConfig())
                .build();

        dockerClient = DockerClientImpl.getInstance(clientConfig, httpClient);


        new BCContainer(this, containerList);
        new LobbyService(this, serviceList);
        new R6Service(this, serviceList);
    }


}
