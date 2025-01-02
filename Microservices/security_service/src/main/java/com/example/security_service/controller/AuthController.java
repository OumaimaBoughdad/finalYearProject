package com.example.security_service.controller;

import com.example.security_service.entity.Employee;
import com.example.security_service.repository.EmployeeRepository;
import com.example.security_service.util.JwtUtil;
import com.example.security_service.util.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepository;
    private final PasswordUtil passwordUtil;

    public AuthController(JwtUtil jwtUtil, EmployeeRepository employeeRepository, PasswordUtil passwordUtil) {
        this.jwtUtil = jwtUtil;
        this.employeeRepository = employeeRepository;
        this.passwordUtil = passwordUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null || !passwordUtil.matches(password, employee.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        String token = jwtUtil.generateToken(employee.getEmail(), employee.getRole());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "employeeId", employee.getIdEmployee(),
                "firstName", employee.getFirstName(),
                "lastName", employee.getLastName(),
                "role", employee.getRole()
        ));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.getUsernameFromToken(token); // email is the username in this context
            String role = jwtUtil.getRoleFromToken(token);
            return ResponseEntity.ok(Map.of("email", email, "role", role));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }
}

