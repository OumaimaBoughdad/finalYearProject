package com.example.security_service;

import com.example.security_service.dto.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private EmployeeClient employeeClient;



    @GetMapping("/employee")
    public ResponseEntity<?> testEmployeeClient(@RequestParam String email) {
        try {
            Employee employee = employeeClient.getEmployeeByEmail(email);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to connect to employee-service: " + e.getMessage());
        }
    }
}

