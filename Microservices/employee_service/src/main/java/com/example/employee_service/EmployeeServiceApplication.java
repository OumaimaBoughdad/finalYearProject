package com.example.employee_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableDiscoveryClient

public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}


	@KafkaListener(topics = "employeeService", groupId = "groupC")
	public void handleEmployee(PetitAdded petitAdded) {
		//send out an email employee
		System.out.println("Received ClientPlacedEvent with Client ID: " + petitAdded.getPetitId());

	}
}
