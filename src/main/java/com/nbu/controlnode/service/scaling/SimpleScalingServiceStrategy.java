package com.nbu.controlnode.service.scaling;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("simple")
public class SimpleScalingServiceStrategy implements ScalingServiceStrategy {
    @Override
    public int getMaxMemoryBeforeScalingUp() {
        return 500;
    }

    @Override
    public int getMinMemoryBeforeScalingDown() {
        return 5;
    }

    @Override
    public int getMinNumberOfKeysBeforeScalingDown() {
        return -1;
    }

    @Override
    public int getMaxNumberOfKeysBeforeScalingUp() {
        return 100;
    }
}
