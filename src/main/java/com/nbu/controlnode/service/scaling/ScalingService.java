package com.nbu.controlnode.service.scaling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.DataNodeManager;
import com.nbu.controlnode.datanode.health.HealthStatus;
import com.nbu.controlnode.service.ConsistentHashingService;

@Component
public class ScalingService {

    private final ScalingServiceStrategy scalingServiceStrategy;
    private final ConsistentHashingService consistentHashingService;
    private final DataNodeManager dataNodeManager;
    private volatile Set<DataNode> currentScalingEffort;

    public ScalingService(@Qualifier("simple") ScalingServiceStrategy scalingServiceStrategy, ConsistentHashingService consistentHashingService, DataNodeManager dataNodeManager) {
        this.scalingServiceStrategy = scalingServiceStrategy;
        this.consistentHashingService = consistentHashingService;
        this.dataNodeManager = dataNodeManager;
        this.currentScalingEffort = new HashSet<>();
    }

    public void checkIfClusterNeedsRebalancing(HealthStatus body, DataNode dataNode) {
        if (currentScalingEffort.isEmpty()) {
            try {
                if (body.getMemoryUsage() > scalingServiceStrategy.getMaxMemoryBeforeScalingUp() || body.getNumberOfKeys() > scalingServiceStrategy.getMaxNumberOfKeysBeforeScalingUp()) {
                    scaleUpAtPosition(dataNode);
                } else if (body.getNumberOfKeys() < scalingServiceStrategy.getMinNumberOfKeysBeforeScalingDown() || body.getMemoryUsage() < scalingServiceStrategy.getMinMemoryBeforeScalingDown()) {
                    scaleDown();
                }
            } catch (Exception e) {
                System.out.println("Error rebalancing!" + e.getMessage());
            }
        }
    }

    private void scaleDown() {
    }

    private void scaleUpAtPosition(DataNode dataNode) {
        currentScalingEffort.add(dataNode);
        System.out.println("Scaling up the cluster based on " + dataNode.toString());
        int position = consistentHashingService.getDnPosition(dataNode);
        if (position == -1) {
            return;
        }
        startDataNodeAtSpecificPosition(position);
    }

    private void startDataNodeAtSpecificPosition(int position) {
        dataNodeManager.addDataNodeAtPosition(position);
    }
}
