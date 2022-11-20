package com.nbu.controlnode.local.docker;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.nbu.controlnode.config.DistributedCacheConfig;

@Component
public class DockerClientFactory {

    final DistributedCacheConfig distributedCacheConfig;

    public DockerClientFactory(DistributedCacheConfig distributedCacheConfig) {
        this.distributedCacheConfig = distributedCacheConfig;
    }

    public DockerClient getDockerClient() {
//        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
//                .withDockerHost("tcp://localhost:2375")
//                .build();
//
//        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
//                .dockerHost(config.getDockerHost())
//                .sslConfig(config.getSSLConfig())
//                .maxConnections(100)
//                .connectionTimeout(Duration.ofSeconds(30))
//                .responseTimeout(Duration.ofSeconds(45))
//                .build();
        DockerClientConfig dockerClientConfig = buildDockerClientConfig(distributedCacheConfig.getConfig().getDockerHost(), distributedCacheConfig.getConfig().getDockerPort());
        return DockerClientImpl.getInstance(dockerClientConfig, buildHTTPClient(dockerClientConfig));
    }

    private DockerClientConfig buildDockerClientConfig(String dockerHost, String dockerPort) {
        String dockerUrl = String.format("tcp://%s:%s", dockerHost, dockerPort);
        return DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerUrl)
                .build();
    }

    private DockerHttpClient buildHTTPClient(DockerClientConfig config) {
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
    }
}
