package com.example.security_service;

import com.example.security_service.dto.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "employeeClient", url = "${employee.service.url}")
public interface EmployeeClient {

    @GetMapping("/employees/by-email")
    Employee getEmployeeByEmail(@RequestParam("email") String email);
}
