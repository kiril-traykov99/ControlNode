package com.nbu.controlnode.datanode;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.nbu.controlnode.controller.rest.Results;
import com.nbu.controlnode.controller.rest.Results.Result;
import com.nbu.controlnode.datanode.health.DataNodeNeedsReplacingNotification;
import com.nbu.controlnode.local.docker.DataNodeAddedNotification;
import com.nbu.controlnode.local.docker.DockerService;

@Component
public class DataNodeManager implements ApplicationListener<DataNodeNeedsReplacingNotification> {

    final DockerService dockerService;
    final ScalingStrategy scalingStrategy = ScalingStrategy.LOCAL;

    public DataNodeManager(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @Override
    public void onApplicationEvent(DataNodeNeedsReplacingNotification event) {
        System.out.println("DN needs replacing notification received");
        if (event.getDataNode().dataNodeType.equals(DataNodeType.Local)) {
            dockerService.replaceDataNode(event.getDataNode());
        }
    }

    public Result addDataNode() {
        try {
            if (scalingStrategy.equals(ScalingStrategy.LOCAL)) {
                dockerService.startDN(false);
            } else if (ScalingStrategy.AWS.equals(scalingStrategy)) {

            } else {
                if (RandomGenerator.getDefault().nextInt() % 2 == 0) {
                    dockerService.startDN(false);
                } else {

                }
            }
        } catch (Exception e) {
            return Results.SERVICE_ERROR;
        }
        return Results.Added;
    }

    public void addDataNodeAtPosition(int position) {
            if (scalingStrategy.equals(ScalingStrategy.LOCAL)) {
                dockerService.startDNAtPosition(position);
            } else if (ScalingStrategy.AWS.equals(scalingStrategy)) {

            } else {
                if (RandomGenerator.getDefault().nextInt() % 2 == 0) {
                    dockerService.startDNAtPosition(position);
                } else {

                }
            }
    }
}
