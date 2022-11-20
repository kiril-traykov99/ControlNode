package com.nbu.controlnode.datanode.health;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.DataNodeEndpoint;

public class DataNodeNeedsReplacingNotification extends ApplicationEvent {

    private final DataNode dataNode;

    public DataNodeNeedsReplacingNotification(Object source, DataNode dataNode) {
        super(source);
        this.dataNode = dataNode;
    }

    public DataNode getDataNode() {
        return dataNode;
    }
}
