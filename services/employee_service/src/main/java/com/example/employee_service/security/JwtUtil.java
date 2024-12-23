package com.example.employee_service.security;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email, String role) {
        logger.info("Generating token with role: " + role + " (class: " + role.getClass().getName() + ")");

        // Force un rôle valide pour débogage
        role = "ROLE_ADMIN"; // À supprimer après le débogage

        Map<String, Object> claims = Map.of(
                "role", role.startsWith("ROLE_") ? role : "ROLE_" + role
        );

        logger.info("Claims to be added to token: " + claims);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 heure
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        Object rawRole = claims.get("role");

        logger.info("Raw role from token claims: " + rawRole + " (class: " + (rawRole != null ? rawRole.getClass().getName() : "null"));

        String role = rawRole != null ? rawRole.toString() : "";
        logger.info("Converted role from token claims: " + role);

        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token, String email) {
        return (extractEmail(token).equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }
}

