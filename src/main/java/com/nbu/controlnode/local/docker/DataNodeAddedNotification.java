package com.nbu.controlnode.local.docker;

import org.springframework.context.ApplicationEvent;

import com.google.common.base.MoreObjects;
import com.nbu.controlnode.datanode.DataNodeEndpoint;

public class DataNodeAddedNotification extends ApplicationEvent {

    private final DataNodeEndpoint dataNodeEndpoint;

    public DataNodeAddedNotification(Object source, DataNodeEndpoint dataNodeEndpoint) {
        super(source);
        this.dataNodeEndpoint = dataNodeEndpoint;
    }

    public DataNodeEndpoint getDataNodeEndpoint() {
        return dataNodeEndpoint;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dataNodeEndpoint", dataNodeEndpoint)
                .toString();
    }
}
