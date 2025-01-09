package com.example.security_services.controller;


import com.example.security_services.dto.AuthRequest;
import com.example.security_services.entity.Employee;
import com.example.security_services.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @PostMapping("/register")
//    public void addNewUser(@RequestBody Employee user) {
//         service.saveUser(user);
//    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam(value = "Bearer") String token) {
        service.validateToken(token);
        return "Token is valid";
    }


}
