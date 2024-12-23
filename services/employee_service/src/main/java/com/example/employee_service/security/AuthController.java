package com.example.employee_service.security;




import com.example.employee_service.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MyUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        System.out.println("Attempting login for email: " + email);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            System.out.println("Authentication successful for email: " + email);

            final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            final String jwt = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().toString());

            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }

}


