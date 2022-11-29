package com.nbu.controlnode.controller.rest;

import java.util.Map;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.nbu.controlnode.datanode.DataNode;

public class Response {

    DataNode dataNode;

    Map<String, Object> data;

    public Response(DataNode dataNode, Map<String, Object> data) {
        this.dataNode = dataNode;
        this.data = data;
    }

    public DataNode getDataNode() {
        return dataNode;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response response = (Response) o;
        return Objects.equals(dataNode, response.dataNode) && Objects.equals(data, response.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataNode, data);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dataNode", dataNode)
                .add("data", data)
                .toString();
    }
}
