package com.example.security_services;

import com.example.security_services.entity.Employee;
import com.example.security_services.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class SecurityServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityServicesApplication.class, args);
	}


	@Autowired
	private PasswordEncoder passwordEncoder;




	@Autowired
	private EmployeeRepository employeeRepository;


	Logger log = LoggerFactory.getLogger(SecurityServicesApplication .class);
	private final JdbcTemplate jdbcTemplate;

	public SecurityServicesApplication ( JdbcTemplate jdbcTemplate,EmployeeRepository employeeRepository) {

		this.jdbcTemplate = jdbcTemplate;
		this.employeeRepository = employeeRepository;
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}




}
