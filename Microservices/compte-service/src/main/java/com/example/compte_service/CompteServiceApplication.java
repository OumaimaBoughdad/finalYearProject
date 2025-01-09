package com.example.compte_service;

import com.example.compte_service.entity.Client;
import com.example.compte_service.entity.ClientEvent;
import com.example.compte_service.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

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


	private final ObjectMapper objectMapper = new ObjectMapper();

//	consume the employee object
	@KafkaListener(topics = "employeeTopic", groupId = "groupDtest")
	public void consumeEmployee(Employee employee){

		log.info("we have received employee with ID: {}", employee.getIdEmployee());
		log.info("we have received employee with name: {}", employee.getFirstName());


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

	@KafkaListener(topics = "employeeupdat", groupId = "groupDtest")
	public void consumeEmployeeupdate(Employee employee){

		log.info("we have received employee with ID: {}", employee.getIdEmployee());
		log.info("we have received employee to update with name: {}", employee.getFirstName());


		long id = employee.getIdEmployee();
		String firstName = employee.getFirstName();
		String lastName = employee.getLastName();
		String email = employee.getEmail();
		String phoneNumber = employee.getPhoneNumber();
		String role = employee.getRole();
		String password = employee.getPassword();


		String sql = "UPDATE employees SET email = ?, first_name = ?, last_name = ?, password = ?, phone_number = ?, role = ? WHERE id_employee = ?";


		try {
			int rowsAffected = jdbcTemplate.update(sql, email, firstName, lastName, password, phoneNumber, role, id);

			if (rowsAffected > 0) {
				log.info("Successfully update employee with ID: {}", id);
			} else {
				log.warn("No employee found with ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Error updating employee with ID: {}", id, e);
		}


	}




	@KafkaListener(topics = "clPost", groupId = "groupDtest")
	public void consumeClient(Client client) {
		log.info("We have received client with ID: {}", client.getIdClient());
		log.info("We have received client with name: {}", client.getFirstName());


		long idClient = client.getIdClient();
		String address = client.getAddress();
		String email = client.getEmail();
		String firstName = client.getFirstName();
		String lastName = client.getLastName();
		String phoneNumber = client.getPhoneNumber();
		  // Assuming Client has employeeId field
		String cne = client.getCne();  // Assuming Client has cne field
		long emplId = 17l;


		String sql = "INSERT INTO clients (id_client, address, email, first_name, last_name, phone_number, employee_id, cne) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			int rowsAffected = jdbcTemplate.update(sql, idClient, address, email, firstName, lastName, phoneNumber, emplId, cne);

			if (rowsAffected > 0) {
				log.info("Successfully inser client with ID: {}", idClient);
			} else {
				log.warn("No client found with ID: {}", idClient);
			}
		} catch (Exception e) {
			log.error("Error inserting client with ID: {}", idClient, e);
		}
	}

	@KafkaListener(topics = "TOPICj", groupId = "groupDtest")
	public void consumeClientdelet(Client client) {
		log.info("We have received client with ID: {}", client.getIdClient());
		log.info("We have received client with name: {}", client.getFirstName());

		long idClient = client.getIdClient();
		String sql = "DELETE FROM clients WHERE id_client = ?"; // Correct column name

		try {
			int rowsAffected = jdbcTemplate.update(sql, idClient);

			if (rowsAffected > 0) {
				log.info("Successfully deleted client with ID: {}", idClient);
			} else {
				log.warn("No client found with ID: {}", idClient);
			}
		} catch (Exception e) {
			log.error("Error deleting client with ID: {}", idClient, e);
		}
	}

	@KafkaListener(topics = "topicupdate", groupId = "grpem")
	public void consumeClientupdate(Client client) {
		log.info("We have received client with ID: {}", client.getIdClient());
		log.info("We have received client with name: {}", client.getFirstName());

		long idClient = client.getIdClient();
		String address = client.getAddress();
		String email = client.getEmail();
		String firstName = client.getFirstName();
		String lastName = client.getLastName();
		String phoneNumber = client.getPhoneNumber();

		String cne = client.getCne();  // Assuming Client has cne field
		long emplId = 17l;

		String sql = "UPDATE clients SET address = ?, email = ?, first_name = ?, last_name = ?, phone_number = ?, cne = ?, employee_id = ? WHERE id_client = ?";

		try {
			int rowsAffected = jdbcTemplate.update(sql, address, email, firstName, lastName, phoneNumber, cne, emplId, idClient);

			if (rowsAffected > 0) {
				log.info("Successfully updated client with ID: {}", idClient);
			} else {
				log.warn("No client found with ID: {}", idClient);
			}
		} catch (Exception e) {
			log.error("Error updating client with ID: {}", idClient, e);
		}
	}

	@KafkaListener(topics = "employeedelet", groupId = "grpem")
	public void consumemployeedelet(Employee employee) {
		log.info("We have received employee with ID: {}", employee.getIdEmployee());
		log.info("We have received employee with name: {}", employee.getFirstName());

		long idEmployee = employee.getIdEmployee();
		String sql = "DELETE FROM employees WHERE id_employee = ?"; // Correct column name

		try {
			int rowsAffected = jdbcTemplate.update(sql, idEmployee);

			if (rowsAffected > 0) {
				log.info("Successfully deleted employee with ID: {}", idEmployee);
			} else {
				log.warn("No employee found with ID: {}", idEmployee);
			}
		} catch (Exception e) {
			log.error("Error deleting employee with ID: {}", idEmployee, e);
		}
	}
}
