//package com.example.compte_service;
//
//import io.netty.handler.codec.http.HttpMethod;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(new AntPathRequestMatcher("/api/compte/**", "PUT")).permitAll() // Autoriser PUT sur /api/compte/**
//                        .anyRequest().authenticated() // Toute autre requête nécessite une authentification
//                );
//        return http.build();
//    }
//}
