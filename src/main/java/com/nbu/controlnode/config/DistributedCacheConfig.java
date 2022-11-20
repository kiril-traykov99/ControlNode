package com.nbu.controlnode.config;

import org.springframework.context.annotation.Configuration;

import com.nbu.controlnode.config.entity.Config;

@Configuration
public class DistributedCacheConfig {

    private final Config config;

    DistributedCacheConfig() {
        config = new Config();
    }

    public Config getConfig() {
        return config;
    }
}
