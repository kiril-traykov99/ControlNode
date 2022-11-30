package com.nbu.controlnode.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.nbu.controlnode.controller.rest.Response;
import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.DataNodeCommunicatorHandler;
import com.nbu.controlnode.service.rehashing.RehashingService;

@Component
public class ConsistentHashingService {

    private static final int totalCirclePositions = 360;
    private final TreeMap<Integer, DataNode> hashCircle = new TreeMap<>();
    private final DataNodeCommunicatorHandler dataNodeCommunicatorHandler;
    private final RehashingService rehashingService;

    public ConsistentHashingService(DataNodeCommunicatorHandler dataNodeCommunicatorHandler, RehashingService rehashingService) {
        this.dataNodeCommunicatorHandler = dataNodeCommunicatorHandler;
        this.rehashingService = rehashingService;
    }

    public void newDNAdded(DataNode dataNode, boolean startingUp) {
        int position = getPosition(dataNode);
        System.out.println("Data node added position:" + position + dataNode.toString());
        hashCircle.put(position, dataNode);
        if (!startingUp) {
            DataNode dataNodeWithKeysToRehash = findFirstDataNodeToRehash(position);
            Map<String, HashMap<String, Object>> rehashingResponseBody = rehashingService.rehashKeys(dataNodeWithKeysToRehash);
            rehashCurrentDNKeys(rehashingResponseBody);
            rehashingService.endRehash(dataNodeWithKeysToRehash);
        }
    }

    private void rehashCurrentDNKeys(Map<String, HashMap<String, Object>> rehashingResponseBody) {
        rehashingResponseBody.forEach(this::writeData);
    }

    public DataNode writeData(String key, HashMap<String, Object> data) {
        DataNode dn = hashKey(key);
        writeKeyToDn(dn, key, data);
        return dn;
    }

    private DataNode hashKey(String key) {
        DataNode dn = null;
        int position = Math.abs(key.hashCode()) % totalCirclePositions;
        SortedMap<Integer, DataNode> tailMap = hashCircle.tailMap(position);
        if (!tailMap.isEmpty()) {
            dn = hashCircle.get(hashCircle.tailMap(position).firstKey());
        }
        System.out.println(position);
        System.out.println(tailMap.size());
        if (dn == null) {
            dn = hashCircle.get(hashCircle.headMap(totalCirclePositions).firstKey());
        }
        System.out.println(dn.toString() + " identified for key " + key + " at position " + position);
        return dn;
    }

    private void writeKeyToDn(DataNode dn, String key, HashMap<String, Object> data) {
        dataNodeCommunicatorHandler.writeKeyToDN(dn, key, data);
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
        if(!dataNode.isPresent()) {
            return hashCircle.get(hashCircle.headMap(position).firstKey());
        }
        return dataNode.get();
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
        rehashCurrentDNKeys(rehashingService.handOverKeysIfPossible(hashCircle.get(dataNode.getDataNodeId().hashCode() % totalCirclePositions)));
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

    public Response readData(String key) {
        DataNode dn = hashKey(key);
        return new Response(dn, dataNodeCommunicatorHandler.readDataFromDn(key, dn));
    }
}
