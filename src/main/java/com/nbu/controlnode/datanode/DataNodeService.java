package com.nbu.controlnode.datanode;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.nbu.controlnode.controller.rest.Results;
import com.nbu.controlnode.controller.rest.Results.Result;
import com.nbu.controlnode.datanode.health.DataNodeReplacedNotification;
import com.nbu.controlnode.local.docker.DataNodeAddedNotification;
import com.nbu.controlnode.service.ConsistentHashingService;

@Service
public class DataNodeService implements ApplicationListener<ApplicationEvent> {

    Set<DataNode> dataNodes = ConcurrentHashMap.newKeySet();
    final ConsistentHashingService hashingService;

    public DataNodeService(ConsistentHashingService hashingService) {
        this.hashingService = hashingService;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof DataNodeAddedNotification addedNotification) {
            Integer position = addedNotification.getPosition();
            DataNode dataNode = new DataNode(addedNotification.getDataNodeEndpoint(), UUID.randomUUID(), DataNodeType.Local, position);
            dataNodes.add(dataNode);
            hashingService.newDNAdded(dataNode);
        } else if (event instanceof DataNodeReplacedNotification dataNodeReplacedNotification) {
            System.out.println("DN replace notification recieved by DN service");
            DataNode dataNode = new DataNode(dataNodeReplacedNotification.getDockerDataNodeEndpoint(), dataNodeReplacedNotification.getId(), DataNodeType.Local, dataNodeReplacedNotification.getPosition());
            DataNode dataNodeToRemove = dataNodes.stream().filter(dn -> dn.dataNodeId.equals(dataNodeReplacedNotification.getId())).findFirst().get();
            dataNodes.remove(dataNodeToRemove);
            dataNodes.add(dataNode);
            hashingService.dnReplaced(dataNode);
        }
    }

    public Set<DataNode> getDataNodes() {
        return dataNodes;
    }

    public Result removeDataNodeFromCluster(UUID dataNodeId) {
        try {
            DataNode dataNode;
            Optional<DataNode> optionalDataNode = dataNodes.stream().filter(dn -> dn.dataNodeId.equals(dataNodeId)).findFirst();
            if (optionalDataNode.isPresent()) {
                dataNode = optionalDataNode.get();
            } else {
                return Results.NodeNotFound;
            }
            dataNodes.remove(dataNode);
            hashingService.dnRemoved(dataNode);
            return Results.Deleted;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Results.SERVICE_ERROR;
        }
    }
}
