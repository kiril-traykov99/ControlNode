package com.nbu.controlnode.datanode.health;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.DataNodeEndpoint;
import com.nbu.controlnode.datanode.DataNodeService;
import com.nbu.controlnode.service.scaling.ScalingService;

@Lazy(false)
@Component
public class DataNodeHealthChecker {

    private final DataNodeService dataNodeService;
    private final ScalingService scalingService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Set<DataNode> unhealthyDataNodeSet = new HashSet<>();

    RestTemplate restTemplate = new RestTemplate();

    public DataNodeHealthChecker(DataNodeService dataNodeService, ScalingService scalingService, ApplicationEventPublisher applicationEventPublisher) {
        this.dataNodeService = dataNodeService;
        this.scalingService = scalingService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void onStartup() {
        checkDataNodeHealth();
    }

    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void checkDataNodeHealth() {
        for (DataNode dataNode : dataNodeService.getDataNodes()) {
            if(!unhealthyDataNodeSet.contains(dataNode)) {
                sendCheckHealthRequest(dataNode);
            }
        }
    }

    public void sendCheckHealthRequest(DataNode dataNode) {
        long start = System.currentTimeMillis();
        ResponseEntity<HealthStatus> response;
        try {
            response = this.restTemplate.getForEntity(buildUrl(dataNode.getDataNodeEndpoint()), HealthStatus.class);
        long finish = System.currentTimeMillis();
        if (response.getStatusCode() == HttpStatus.OK && finish - start < 1000) {
            scalingService.checkIfClusterNeedsRebalancing(response.getBody(), dataNode);
            System.out.println(dataNode.getDataNodeId() + " is healthy");
        } else {
            unhealthyDataNodeSet.add(dataNode);
            markNodeAsUnhealthy(dataNode);
        }
        } catch (Exception e) {
            unhealthyDataNodeSet.add(dataNode);
            markNodeAsUnhealthy(dataNode);
        }
    }

    private void markNodeAsUnhealthy(DataNode dataNode) {
        System.out.println("Data node at port " + dataNode.getDataNodeEndpoint().getDataNodeContactPoint().port() + "needs replacing");
        applicationEventPublisher.publishEvent(new DataNodeNeedsReplacingNotification(this, dataNode));
    }

    private String buildUrl(DataNodeEndpoint dataNodeEndpoint) {
        return String.format("http://%s:%d/v1/api/health", dataNodeEndpoint.getDataNodeContactPoint().url(), dataNodeEndpoint.getDataNodeContactPoint().port());
    }
}
