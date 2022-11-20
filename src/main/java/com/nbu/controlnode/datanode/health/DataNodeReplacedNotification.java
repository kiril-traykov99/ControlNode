package com.nbu.controlnode.datanode.health;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import com.nbu.controlnode.local.docker.DockerDataNodeEndpoint;

public class DataNodeReplacedNotification extends ApplicationEvent {

    private final UUID id;
    private final DockerDataNodeEndpoint dockerDataNodeEndpoint;

    public DataNodeReplacedNotification(Object source, UUID dataNodeId, DockerDataNodeEndpoint endpoint) {
        super(source);
        this.id = dataNodeId;
        this.dockerDataNodeEndpoint = endpoint;
    }

    public UUID getId() {
        return id;
    }

    public DockerDataNodeEndpoint getDockerDataNodeEndpoint() {
        return dockerDataNodeEndpoint;
    }
}
