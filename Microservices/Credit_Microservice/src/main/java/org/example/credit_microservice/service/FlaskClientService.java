package org.example.credit_microservice.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Map;

@Service
public class FlaskClientService {

    public int predictLoanStatus(Map<String, Object> loanApplication) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5000/predict";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(loanApplication, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(flaskUrl, request, Integer.class);

        return response.getBody();
    }
}
