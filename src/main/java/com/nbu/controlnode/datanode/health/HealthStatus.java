package com.nbu.controlnode.datanode.health;

import java.io.Serializable;
import java.util.Objects;

import com.google.common.base.MoreObjects;

public class HealthStatus implements Serializable {

    int memoryUsage;
    int numberOfKeys;

    public HealthStatus() {
    }

    public HealthStatus(int memoryUsage, int numberOfKeys) {
        this.memoryUsage = memoryUsage;
        this.numberOfKeys = numberOfKeys;
    }

    public int getMemoryUsage() {
        return memoryUsage;
    }

    public int getNumberOfKeys() {
        return numberOfKeys;
    }

    public void setMemoryUsage(int memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public void setNumberOfKeys(int numberOfKeys) {
        this.numberOfKeys = numberOfKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HealthStatus that = (HealthStatus) o;
        return memoryUsage == that.memoryUsage && numberOfKeys == that.numberOfKeys;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memoryUsage, numberOfKeys);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("memoryUsage", memoryUsage)
                .add("numberOfKeys", numberOfKeys)
                .toString();
    }
}
