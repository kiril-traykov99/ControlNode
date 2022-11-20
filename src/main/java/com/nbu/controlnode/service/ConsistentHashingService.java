package com.nbu.controlnode.service;

import java.util.Optional;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.nbu.controlnode.datanode.DataNode;

@Component
public class ConsistentHashingService {

    private static final int totalCirclePositions = 360;
    private final TreeMap<Integer, DataNode> hashCircle = new TreeMap<>();

    private final RehashingService rehashingService;

    public ConsistentHashingService(RehashingService rehashingService) {
        this.rehashingService = rehashingService;
    }

    public void newDNAdded(DataNode dataNode) {
        int position = getPosition(dataNode);
        DataNode dataNodeWithKeysToRehash = findFirstDataNodeToRehash(position);
        System.out.println("Data node added position:" + position + dataNode.toString());
        hashCircle.put(position, dataNode);
        rehashingService.rehashKeys(dataNodeWithKeysToRehash);
        
    }

    private static int getPosition(DataNode dataNode) {
        int position = dataNode.getDataNodeId().hashCode() % totalCirclePositions;
        if (position < 0) {
            position *= -1;
        }
        return position;
    }

    private DataNode findFirstDataNodeToRehash(int position) {
        Optional<DataNode> dataNode = hashCircle.tailMap(position).values().stream().findFirst();
        return dataNode.isPresent() ? dataNode.get() : null;
    }

    public void dnRemoved(DataNode dataNode) {
        int position = getPosition(dataNode);
        System.out.println("Data node removed position:" + position + dataNode.toString());
        hashCircle.remove(position);
        rehashingService.rehashKeys(dataNode);
    }

    public void dnReplaced(DataNode dataNode) {
        int position = getPosition(dataNode);
        System.out.println("Data node replaced position:" + position + dataNode.toString());
        rehashingService.handOverKeysIfPossible(hashCircle.get(dataNode.getDataNodeId().hashCode() % totalCirclePositions));
        hashCircle.put(position, dataNode);
    }
}
