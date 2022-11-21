package com.nbu.controlnode.service.scaling;

public interface ScalingServiceStrategy {

    int getMaxMemoryBeforeScalingUp();
    int getMinMemoryBeforeScalingDown();
    int getMinNumberOfKeysBeforeScalingDown();
    int getMaxNumberOfKeysBeforeScalingUp();

}
