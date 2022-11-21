package com.nbu.controlnode.local.docker;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;

import com.nbu.controlnode.config.DistributedCacheConfig;
import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.health.DataNodeReplacedNotification;
import com.nbu.controlnode.service.StartUpService;

@Service
public class DockerService {

    private final String imageName;
    private final DockerClient dockerClient;
    private volatile int freePortCounter = 8081; //Port to which the container would bind, 8080 is the control node so 8081 is the first available
    private static final int dataNodePort = 8085; //The container's exposed port before binding
    private static final String dataNodeContainerNameTemplate = "dataNode";

    private final ApplicationEventPublisher applicationEventPublisher;

    DockerService(DistributedCacheConfig distributedCacheConfig, DockerClientFactory dockerClientFactory, ApplicationEventPublisher applicationEventPublisher) {
        this.imageName = distributedCacheConfig.getConfig().getImageName();
        this.dockerClient = dockerClientFactory.getDockerClient();
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private ExposedPort bindPort(Ports portBindings) {
        ExposedPort portForCurrentContainer = ExposedPort.tcp(dataNodePort);
        portBindings.bind(portForCurrentContainer, Ports.Binding.bindPort(freePortCounter));
        return portForCurrentContainer;
    }

    public void doStuff() {
        System.out.println("request hit");

    }

    public void startDN() {
        CreateContainerResponse createContainerResponse = prepareContainerObject();
        dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
        applicationEventPublisher.publishEvent(new DataNodeAddedNotification(this, new DockerDataNodeEndpoint("localhost", freePortCounter), null));
        freePortCounter++;
    }

    private CreateContainerResponse prepareContainerObject() {
        Ports portBindings = new Ports();
        ExposedPort tcpPort = bindPort(portBindings);
        UUID uuidd = UUID.randomUUID();
        return dockerClient.createContainerCmd(imageName)
                .withExposedPorts(tcpPort)
                .withHostConfig(newHostConfig()
                        .withPortBindings(portBindings))
                .withName(String.format("%s%d-%s", dataNodeContainerNameTemplate, freePortCounter, uuidd.toString()))
                .exec();
    }

    public void replaceDataNode(DataNode dataNode) {
        CreateContainerResponse createContainerResponse = prepareContainerObject();
        dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
        System.out.println("new dn created");
        System.out.println(freePortCounter);
        StartUpService service = new StartUpService();
        service.awaitForStartup(freePortCounter);
        applicationEventPublisher.publishEvent(new DataNodeReplacedNotification(this, dataNode.getDataNodeId(), new DockerDataNodeEndpoint("localhost", freePortCounter), dataNode.getPosition()));
        freePortCounter++;
    }

    public void startDNAtPosition(int position) {
        CreateContainerResponse createContainerResponse = prepareContainerObject();
        dockerClient.startContainerCmd(createContainerResponse.getId()).exec();
        StartUpService service = new StartUpService();
        service.awaitForStartup(freePortCounter);
        applicationEventPublisher.publishEvent(new DataNodeAddedNotification(this, new DockerDataNodeEndpoint("localhost", freePortCounter), position));
        freePortCounter++;
    }
}
