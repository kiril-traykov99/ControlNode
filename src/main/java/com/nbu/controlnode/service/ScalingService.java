package com.nbu.controlnode.service;

import org.springframework.stereotype.Component;

import com.nbu.controlnode.datanode.health.HealthStatus;

@Component
public class ScalingService {
    public void checkIfClusterNeedsRebalancing(HealthStatus body) {
    }
}
