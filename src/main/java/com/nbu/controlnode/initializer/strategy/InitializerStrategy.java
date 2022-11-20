package com.nbu.controlnode.initializer.strategy;

import org.springframework.stereotype.Component;

@Component
public interface InitializerStrategy {

    public void initialize();
}
