package com.nbu.controlnode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nbu.controlnode.local.docker.DockerService;

@RestController
@RequestMapping("/v1/api")
public class DistributionController {

    final
    DockerService dockerService;

    public DistributionController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @RequestMapping("/data")
    public String requestData() {
        dockerService.doStuff();
        return "chep";
    }

}
