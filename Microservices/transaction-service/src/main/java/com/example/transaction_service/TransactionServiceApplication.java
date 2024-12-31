package com.example.transaction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class TransactionServiceApplication {

	@Bean
	public WebClient webclient(){
		return WebClient.builder().baseUrl("http://localhost:8080").build();
	}


	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

}
