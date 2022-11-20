package com.nbu.controlnode.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nbu.controlnode.datanode.health.HealthStatus;

@Service
public class StartUpService {

    RestTemplate restTemplate = new RestTemplate();


    public void awaitForStartup(int dnPort) {
        while (true) {
            ResponseEntity<HealthStatus> response = null;
            try {
                response = this.restTemplate.getForEntity(buildUrl(dnPort), HealthStatus.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    return;
                } else {
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }

    private String buildUrl(int dataNodePort) {
        return String.format("http://%s:%d/v1/api/health", "localhost", dataNodePort);
    }
}
