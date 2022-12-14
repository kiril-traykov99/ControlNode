package com.nbu.controlnode.datanode;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DataNodeCommunicatorHandler {

    RestTemplate restTemplate = new RestTemplate();

    public void writeKeyToDN(DataNode dataNode, String key, HashMap<String, Object> value) {
        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request;
        try {
            request = new HttpEntity<>(new ObjectMapper().writeValueAsString(value), headers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(value.toString());
        String url = buildUrl(dataNode.getDataNodeEndpoint(), key);
        System.out.println("Trying to post write request");
        System.out.println(url);
        this.restTemplate.postForEntity(url, request, String.class);

    }

    private String buildUrl(DataNodeEndpoint dataNodeEndpoint, String key) {
        return String.format("http://%s:%d/v1/api/data/%s", dataNodeEndpoint.getDataNodeContactPoint().url(), dataNodeEndpoint.getDataNodeContactPoint().port(), key);
    }

    public Map<String, Object> readDataFromDn(String key, DataNode dn) {
        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request;
            request = new HttpEntity<>(null, headers);
        String url = buildUrl(dn.getDataNodeEndpoint(), key);
        System.out.println("Trying to execute get request");
        System.out.println(url);
        Map<String, Object> r = this.restTemplate.getForObject(url, Map.class);

        return r;

    }
}
