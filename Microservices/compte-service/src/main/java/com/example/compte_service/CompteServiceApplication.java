package com.example.compte_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CompteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompteServiceApplication.class, args);
	}

}
