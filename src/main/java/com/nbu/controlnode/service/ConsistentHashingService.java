package com.nbu.controlnode.service;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.service.rehashing.RehashingResponseBody;
import com.nbu.controlnode.service.rehashing.RehashingService;

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
        RehashingResponseBody rehashingResponseBody = rehashingService.rehashKeys(dataNodeWithKeysToRehash);
    }

    private static int getPosition(DataNode dataNode) {
        if (dataNode.getPosition() != null) {
            return dataNode.getPosition();
        }
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

    public int getDnPosition(DataNode dataNode) {

        int position = getPosition(dataNode);
        SortedMap<Integer, DataNode> previousNodes = hashCircle.headMap(position);

        if (previousNodes.size() != 0) {
            int previousPosition = previousNodes.lastKey();
            int middle = (position + previousPosition) / 2;
            if (middle == previousPosition || middle == position) {
                return -1;
            }
            return middle;
        }

        int previousNodePosition = hashCircle.tailMap(position).lastKey();
        int middle = (((previousNodePosition + position) / 2) + totalCirclePositions / 2) % totalCirclePositions;
        if (middle == position || middle == previousNodePosition) {
            return -1;
        }
        return middle;
    }

    public TreeMap<Integer, DataNode> getHashCircle() {
        return hashCircle;
    }
}
