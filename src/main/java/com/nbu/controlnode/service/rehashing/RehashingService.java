package com.nbu.controlnode.service.rehashing;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nbu.controlnode.datanode.DataNode;
import com.nbu.controlnode.datanode.DataNodeEndpoint;

@Service
public class RehashingService {

    RestTemplate restTemplate = new RestTemplate();

    public Map<String, HashMap<String, Object>> rehashKeys(DataNode dataNodeWithKeysToRehash) {
        if (dataNodeWithKeysToRehash == null) {
            return null;
        }

        Map<String, HashMap<String, Object>> result = restTemplate.getForObject(buildUrl(dataNodeWithKeysToRehash.getDataNodeEndpoint()),Map.class);
        System.out.println(result);
        return result;
    }

    private String buildUrl(DataNodeEndpoint dataNodeEndpoint) {
        return String.format("http://%s:%d/v1/api/rehash", dataNodeEndpoint.getDataNodeContactPoint().url(), dataNodeEndpoint.getDataNodeContactPoint().port());
    }

    public void handOverKeysIfPossible(DataNode dataNode) {
    }

    public void endRehash(DataNode dataNodeWithKeysToRehash) {
        restTemplate.getForObject(buildUrl(dataNodeWithKeysToRehash.getDataNodeEndpoint())+"/end", Object.class);
    }
}
