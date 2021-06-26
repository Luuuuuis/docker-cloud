package de.luuuuuis.proxy.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.Getter;
import lombok.SneakyThrows;

public class Docker {

    @Getter
    private final DockerClient dockerClient;

    @SneakyThrows
    public Docker(String host) {
        DockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                //.withDockerHost("tcp://localhost:2375")
                //.withDockerHost("tcp://proxy.luis.team:2376")
                .withDockerHost(host)
                .withDockerTlsVerify(true)
                .withDockerCertPath("/plugins/certs")
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(clientConfig.getDockerHost())
                .sslConfig(clientConfig.getSSLConfig())
                .build();

        dockerClient = DockerClientImpl.getInstance(clientConfig, httpClient);
    }

}
