package com.nbu.controlnode.initializer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nbu.controlnode.initializer.strategy.InitializerStrategy;

@Component
public class Initializer {

final InitializerStrategy initializerStrategy;

    public Initializer(@Qualifier("local") InitializerStrategy initializerStrategy) {
        this.initializerStrategy = initializerStrategy;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDataCluster() {
        initializerStrategy.initialize();
        System.out.println("hello world, I have just started up");
    }

}
