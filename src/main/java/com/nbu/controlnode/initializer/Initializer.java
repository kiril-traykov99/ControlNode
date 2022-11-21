package com.nbu.controlnode.initializer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.DataNodeType;
import com.nbu.controlnode.initializer.strategy.InitializerStrategy;
import com.nbu.controlnode.local.docker.DockerDataNodeEndpoint;
import com.nbu.controlnode.service.ConsistentHashingService;

@Component
public class Initializer {

private final InitializerStrategy initializerStrategy;
private final ConsistentHashingService consistentHashingService;

    public Initializer(@Qualifier("local") InitializerStrategy initializerStrategy, ConsistentHashingService consistentHashingService) {
        this.initializerStrategy = initializerStrategy;
        this.consistentHashingService = consistentHashingService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDataCluster() {
        initializerStrategy.initialize();
        System.out.println("hello world, I have just started up");
//        consistentHashingService.newDNAdded(new DataNode(new DockerDataNodeEndpoint("localhost", 8085), UUID.randomUUID(), DataNodeType.Local, null));

    }

}
