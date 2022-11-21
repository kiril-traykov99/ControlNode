package com.nbu.controlnode.datanode.health;

import java.util.Objects;
import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import com.google.common.base.MoreObjects;
import com.nbu.controlnode.local.docker.DockerDataNodeEndpoint;

public class DataNodeReplacedNotification extends ApplicationEvent {

    private final UUID id;
    private final DockerDataNodeEndpoint dockerDataNodeEndpoint;
    private final Integer position;

    public DataNodeReplacedNotification(Object source, UUID dataNodeId, DockerDataNodeEndpoint endpoint, Integer position) {
        super(source);
        this.id = dataNodeId;
        this.dockerDataNodeEndpoint = endpoint;
        this.position = position;
    }

    public UUID getId() {
        return id;
    }

    public DockerDataNodeEndpoint getDockerDataNodeEndpoint() {
        return dockerDataNodeEndpoint;
    }

    public Integer getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataNodeReplacedNotification that = (DataNodeReplacedNotification) o;
        return Objects.equals(id, that.id) && Objects.equals(dockerDataNodeEndpoint, that.dockerDataNodeEndpoint) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dockerDataNodeEndpoint, position);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("dockerDataNodeEndpoint", dockerDataNodeEndpoint)
                .add("position", position)
                .toString();
    }
}
