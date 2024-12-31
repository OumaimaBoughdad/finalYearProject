package com.example.security_service.controller;


import com.example.security_service.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/security")
public class TestController {

    @Autowired
    private TestService testService;


    @GetMapping
    public ResponseEntity<Map<String, Object>> getSecurity() {
        return testService.getJsonResponse();

    }


}
