package com.nbu.controlnode.local.docker;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.nbu.controlnode.datanode.DataNodeEndpoint;

public class DockerDataNodeEndpoint extends DataNodeEndpoint {

    DataNodeContactPoint dataNodeContactPoint;
    public DockerDataNodeEndpoint(String url, int port) {
        dataNodeContactPoint = new DataNodeContactPoint(url, port);
    }
    @Override
    public DataNodeContactPoint getDataNodeContactPoint() {
        return dataNodeContactPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DockerDataNodeEndpoint that = (DockerDataNodeEndpoint) o;
        return Objects.equals(dataNodeContactPoint, that.dataNodeContactPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataNodeContactPoint);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dataNodeContactPoint", dataNodeContactPoint)
                .toString();
    }
}
