package com.example.transaction_service;

import com.example.transaction_service.entity.Employee;
import com.example.transaction_service.entity.Compte;
import com.example.transaction_service.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@SpringBootApplication
public class TransactionServiceApplication {


	@Autowired
    private JdbcTemplate jdbcTemplate;


	@Autowired
	private static TransactionService transactionService;

	Logger log = LoggerFactory.getLogger(TransactionServiceApplication.class);

	@Bean
	public WebClient webclient(){
		return WebClient.builder().build();
	}




	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);


	}


	// consume the employee object
	@KafkaListener(topics = "employeeTopic", groupId = "grpemployee")
	public void consumeEmployee(Employee employee){

		log.info("we have received employee to insert with ID: {}", employee.getIdEmployee());
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

	@KafkaListener(topics = "employeeupdat", groupId = "grpemployee")
	public void consumeEmployeeupdate(Employee employee){

		log.info("we have received employee to update with ID: {}", employee.getIdEmployee());
		log.info("we have received employee with name: {}", employee.getFirstName());


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


	@KafkaListener(topics = "employeedelet", groupId = "grpemployee")
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
