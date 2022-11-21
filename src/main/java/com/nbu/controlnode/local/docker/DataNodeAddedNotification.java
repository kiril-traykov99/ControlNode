package com.nbu.controlnode.local.docker;

import org.springframework.context.ApplicationEvent;

import com.google.common.base.MoreObjects;
import com.nbu.controlnode.datanode.DataNodeEndpoint;

public class DataNodeAddedNotification extends ApplicationEvent {

    private final DataNodeEndpoint dataNodeEndpoint;
    private final Integer position;

    public DataNodeAddedNotification(Object source, DataNodeEndpoint dataNodeEndpoint, Integer position) {
        super(source);
        this.dataNodeEndpoint = dataNodeEndpoint;
        this.position = position;
    }

    public DataNodeEndpoint getDataNodeEndpoint() {
        return dataNodeEndpoint;
    }

    public Integer getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dataNodeEndpoint", dataNodeEndpoint)
                .add("position", position)
                .toString();
    }
}
