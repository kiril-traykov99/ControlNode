package com.nbu.controlnode.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nbu.controlnode.controller.rest.Response;
import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.local.docker.DockerService;
import com.nbu.controlnode.service.ConsistentHashingService;

@RestController
@RequestMapping("/v1/api")
public class DistributionController {

    final ConsistentHashingService consistentHashingService;

    public DistributionController(ConsistentHashingService consistentHashingService) {
        this.consistentHashingService = consistentHashingService;
    }

    @RequestMapping(value = "/data/{key}", method = RequestMethod.GET)
    public Response requestData(@PathVariable String key) {
        return consistentHashingService.readData(key);
    }

    @RequestMapping(value = "/data/{key}", method = RequestMethod.POST)
    public DataNode postData(@PathVariable String key, @RequestBody HashMap<String, Object> data) {
        return consistentHashingService.writeData(key, data);
    }

}
