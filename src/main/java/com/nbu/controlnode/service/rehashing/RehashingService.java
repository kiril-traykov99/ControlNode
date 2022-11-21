package com.nbu.controlnode.service.rehashing;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.DataNodeEndpoint;

@Service
public class RehashingService {

    RestTemplate restTemplate = new RestTemplate();

    public RehashingResponseBody rehashKeys(DataNode dataNodeWithKeysToRehash) {
        if (dataNodeWithKeysToRehash == null) {
            return null;
        }
        ResponseEntity<RehashingResponseBody> response;
        response = this.restTemplate.getForEntity(buildUrl(dataNodeWithKeysToRehash.getDataNodeEndpoint()), RehashingResponseBody.class);
        response.getBody().jsonData.entrySet().forEach(entry -> System.out.println(entry.toString()));
        return response.getBody();
    }

    private String buildUrl(DataNodeEndpoint dataNodeEndpoint) {
        return String.format("http://%s:%d/v1/api/rehash", dataNodeEndpoint.getDataNodeContactPoint().url(), dataNodeEndpoint.getDataNodeContactPoint().port());
    }

    public void handOverKeysIfPossible(DataNode dataNode) {
    }
}
