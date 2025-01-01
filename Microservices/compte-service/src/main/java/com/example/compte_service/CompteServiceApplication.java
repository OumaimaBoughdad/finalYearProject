package com.example.compte_service;

import com.example.compte_service.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableDiscoveryClient
public class CompteServiceApplication {


//	@Bean
//	WebClient webClient() {
//
//	}


	@Autowired
	private JdbcTemplate jdbcTemplate;

	Logger log = LoggerFactory.getLogger(CompteServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CompteServiceApplication.class, args);
	}



	//consume the employee object
	// consume the employee object
	@KafkaListener(topics = "employeeTopic", groupId = "groupD")
	public void consumeEmployee(Employee employee){

		log.info("we have received student with ID: {}", employee.getIdEmployee());
		log.info("we have received student with name: {}", employee.getFirstName());


		long id = employee.getIdEmployee();
		String firstName = employee.getFirstName();
		String lastName = employee.getLastName();
		String email = employee.getEmail();
		String phoneNumber = employee.getPhoneNumber();
		String role = employee.getRole();
		String password = employee.getPassword();


		String sql ="INSERT INTO employees(id_employee,email,first_name, last_name, password, phone_number, role) VALUES (?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, id,email, firstName, lastName, password, phoneNumber, role);

	}

}
