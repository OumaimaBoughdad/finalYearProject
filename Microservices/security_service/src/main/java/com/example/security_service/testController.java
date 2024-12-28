package com.example.security_service;


import jakarta.persistence.EntityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class testController {

    @GetMapping("/json/")
    public ResponseEntity<Object> returnJson() {
        // Create a sample JSON object
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello, this is a JSON response!");
        response.put("status", "success");
        response.put("timestamp", LocalDateTime.now());

        // Return the response with HTTP status 200 (OK)
        return ResponseEntity.ok(response);
    }



}
