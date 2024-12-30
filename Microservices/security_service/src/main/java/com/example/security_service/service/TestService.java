package com.example.security_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {

    @Autowired
    private final WebClient webClient;

    public TestService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseEntity<Map<String, Object>> getJsonResponse() {
        try {
            // Make a GET request to check if the employee exists
            Map exists = webClient.get()
                    .uri("http://localhost:8080/api/employees/1")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // If the employee exists, return the JSON response
            if (Map.class.isInstance(exists)) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", 1);
                response.put("name", "John Doe");
                response.put("email", "john.doe@example.com");

                return ResponseEntity.ok(response);
            } else {
                // If the employee does not exist, throw an exception
                throw new RuntimeException("Employee not found");
            }
        } catch (WebClientResponseException e) {
            // Handle cases where the WebClient request fails
            throw new RuntimeException("Error while fetching employee: " + e.getMessage(), e);
        }
    }
}
