package com.nbu.controlnode.controller;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nbu.controlnode.controller.rest.Results;
import com.nbu.controlnode.controller.rest.Results.Failure;
import com.nbu.controlnode.controller.rest.Results.Result;
import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.DataNodeManager;
import com.nbu.controlnode.datanode.DataNodeService;

@RestController
@RequestMapping("/v1/api")
public class ClusterController {

    private final DataNodeService dataNodeService;
    private final DataNodeManager dataNodeManager;

    public ClusterController(DataNodeService dataNodeService, DataNodeManager dataNodeManager) {
        this.dataNodeService = dataNodeService;
        this.dataNodeManager = dataNodeManager;
    }

    @RequestMapping(value = "/dataNodes", method = RequestMethod.GET)
    public ResponseEntity<Collection<DataNode>> getDataNode() {
        Set<DataNode> dataNodeSet = dataNodeService.getDataNodes();

        return new ResponseEntity<>(dataNodeSet, HttpStatus.OK);
    }

    @RequestMapping(value = "/dataNodes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeDataNode(@PathVariable String id) {
        Result result = dataNodeService.removeDataNodeFromCluster(UUID.fromString(id));
        if (result instanceof Failure failure) {
            return new ResponseEntity<>(handleFailure(failure));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/dataNodes", method = RequestMethod.POST)
    public ResponseEntity<Void> addDataNode() {
        Result result = dataNodeManager.addDataNode();
        if (result instanceof Failure failure) {
            return new ResponseEntity<>(handleFailure(failure));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private HttpStatus handleFailure(Failure failure) {
        if (Results.NodeNotFound.equals(failure)) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
