package com.nbu.controlnode.local.docker;

import java.util.Objects;

import org.springframework.context.ApplicationEvent;

import com.google.common.base.MoreObjects;
import com.nbu.controlnode.datanode.DataNodeEndpoint;

public class DataNodeAddedNotification extends ApplicationEvent {

    private final DataNodeEndpoint dataNodeEndpoint;
    private final Integer position;
    private final boolean isStartingUp;

    public DataNodeAddedNotification(Object source, DataNodeEndpoint dataNodeEndpoint, Integer position, boolean isStartingUp) {
        super(source);
        this.dataNodeEndpoint = dataNodeEndpoint;
        this.position = position;
        this.isStartingUp = isStartingUp;
    }

    public DataNodeEndpoint getDataNodeEndpoint() {
        return dataNodeEndpoint;
    }

    public Integer getPosition() {
        return position;
    }

    public boolean isStartingUp() {
        return isStartingUp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataNodeAddedNotification that = (DataNodeAddedNotification) o;
        return isStartingUp == that.isStartingUp && Objects.equals(dataNodeEndpoint, that.dataNodeEndpoint) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataNodeEndpoint, position, isStartingUp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dataNodeEndpoint", dataNodeEndpoint)
                .add("position", position)
                .add("isStartingUp", isStartingUp)
                .toString();
    }
}
