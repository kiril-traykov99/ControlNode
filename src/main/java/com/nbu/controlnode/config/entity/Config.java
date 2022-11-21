package com.nbu.controlnode.config.entity;

public class Config {

    public Config() {
        dockerHost = "localhost";
        dockerPort = "2375";
        imageName = "dn06";
        replicationFactor = 1;
    }

    private String imageName;

    private String dockerHost;

    private String dockerPort;

    private int replicationFactor;

    public String getDockerHost() {
        return dockerHost;
    }

    public String getDockerPort() {
        return dockerPort;
    }

    public String getImageName() {
        return imageName;
    }
}
