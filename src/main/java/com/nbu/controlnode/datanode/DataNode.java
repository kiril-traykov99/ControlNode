package com.nbu.controlnode.datanode;

import java.util.Objects;
import java.util.UUID;

import com.google.common.base.MoreObjects;

public class DataNode {

    DataNodeEndpoint dataNodeEndpoint;
    UUID dataNodeId;
    DataNodeType dataNodeType;
    Integer position;

    public DataNode(DataNodeEndpoint dataNodeEndpoint, UUID dataNodeId, DataNodeType dataNodeType, Integer position) {
        this.dataNodeEndpoint = dataNodeEndpoint;
        this.dataNodeId = dataNodeId;
        this.dataNodeType = dataNodeType;
        this.position = position;
    }

    public DataNodeEndpoint getDataNodeEndpoint() {
        return dataNodeEndpoint;
    }

    public void setDataNodeEndpoint(DataNodeEndpoint dataNodeEndpoint) {
        this.dataNodeEndpoint = dataNodeEndpoint;
    }

    public UUID getDataNodeId() {
        return dataNodeId;
    }

    public void setDataNodeId(UUID dataNodeId) {
        this.dataNodeId = dataNodeId;
    }

    public DataNodeType getDataNodeType() {
        return dataNodeType;
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
        DataNode dataNode = (DataNode) o;
        return Objects.equals(dataNodeEndpoint, dataNode.dataNodeEndpoint) && Objects.equals(dataNodeId, dataNode.dataNodeId) && dataNodeType == dataNode.dataNodeType && Objects.equals(position, dataNode.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataNodeEndpoint, dataNodeId, dataNodeType, position);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dataNodeEndpoint", dataNodeEndpoint)
                .add("dataNodeId", dataNodeId)
                .add("dataNodeType", dataNodeType)
                .add("position", position)
                .toString();
    }
}
