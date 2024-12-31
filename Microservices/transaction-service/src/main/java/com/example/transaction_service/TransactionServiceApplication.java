package com.example.transaction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
=======
>>>>>>> 2e6072705842b7e7377adb0b77b2ceb504e892ba

@SpringBootApplication
public class TransactionServiceApplication {

<<<<<<< HEAD
	@Bean
	public WebClient webclient(){
		return WebClient.builder().baseUrl("http://localhost:8080").build();
	}


=======
>>>>>>> 2e6072705842b7e7377adb0b77b2ceb504e892ba
	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

}
