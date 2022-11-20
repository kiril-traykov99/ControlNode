package com.nbu.controlnode.datanode;

import com.nbu.controlnode.local.docker.DataNodeContactPoint;

public abstract class DataNodeEndpoint {

    public abstract DataNodeContactPoint getDataNodeContactPoint();

}
