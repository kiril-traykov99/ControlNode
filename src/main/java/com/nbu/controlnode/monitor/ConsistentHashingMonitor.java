package com.nbu.controlnode.monitor;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.service.ConsistentHashingService;

@RestController
@RequestMapping("/v1/api")
public class ConsistentHashingMonitor {

    final ConsistentHashingService consistentHashingService;

    public ConsistentHashingMonitor(ConsistentHashingService consistentHashingService) {
        this.consistentHashingService = consistentHashingService;
    }

    @RequestMapping(value = "/ring", method = RequestMethod.GET)
    public TreeMap<Integer, DataNode> getDataNode() {
        return consistentHashingService.getHashCircle();

    }
}
