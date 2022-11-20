package com.nbu.controlnode.initializer.strategy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nbu.controlnode.local.docker.DockerService;

@Component
@Qualifier("local")
public class LocalInitializer implements InitializerStrategy {

    private final DockerService dockerService;
    private final int initialDataNodes;

    public LocalInitializer(DockerService dockerService) {
        this.dockerService = dockerService;
        initialDataNodes = 3;
    }

    @Override
    public void initialize() {
        for (int i = 0; i < initialDataNodes; i++) {
            dockerService.startDN();
        }
    }
}
