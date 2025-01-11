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

import java.time.LocalDate;
import java.util.Date;


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

//events of compts:

	@KafkaListener(topics = "comptadd", groupId = "grpemployee")
	public void consumcomptetoadd(Compte compte) {
		try {
			log.info("we have received compte to insert with ID: {}", compte.getIdCompte());
			log.info("we have received compte to insert with Type: {}", compte.getTypeCompte());

			long id = compte.getIdCompte();
			String num = compte.getNumeroCompte();
			double solde = compte.getSolde();
			Compte.TypeCompte type = compte.getTypeCompte();
			LocalDate date = compte.getDateOuverture();
			double taux = compte.getTaux();
			double decouvert = compte.getDecouvert();

			String sql = "INSERT INTO comptes(id_compte, date_ouverture, decouvert, numero_compte, solde, taux, type_compte) VALUES (?,?,?,?,?,?,?)";
			jdbcTemplate.update(sql, id, date, decouvert, num, solde, taux, type.toString());

			log.info("Compte inserted successfully.");
		} catch (Exception e) {
			log.error("Error processing message: {}", e.getMessage(), e);
			// Optionnel : notifier ou stocker le message pour traitement manuel ultérieur.
		}
	}

	@KafkaListener(topics = "compteup", groupId = "grpemployee")
	public void consumcomptetoup(Compte compte){

		log.info("we have received compte to update with ID: {}", compte.getIdCompte());



		long id = compte.getIdCompte();
		String num= compte.getNumeroCompte();
		double solde = compte.getSolde();

		LocalDate date = compte.getDateOuverture();
		double taux = compte.getTaux();
		double decouvert = compte.getDecouvert();

		String sql = "UPDATE comptes SET   date_ouverture = ? ,decouvert=?, numero_compte=?,solde=?, taux=? WHERE id_compte = ? ";
		jdbcTemplate.update(sql,date, decouvert,num, solde, taux, id);
		log.info("compte update with success");

	}
	@KafkaListener(topics = "comptdelet", groupId = "grpemployee")
	public void consumcomptedelet(Compte compte) {
		log.info("We have received compte to delet with ID: {}", compte.getIdCompte());


		long idCompte = compte.getIdCompte();
		String sql = "DELETE FROM comptes WHERE id_compte = ?"; // Correct column name

		try {
			int rowsAffected = jdbcTemplate.update(sql, idCompte);

			if (rowsAffected > 0) {
				log.info("Successfully deleted compte with ID: {}", idCompte);
			} else {
				log.warn("No compte found with ID: {}", idCompte);
			}
		} catch (Exception e) {
			log.error("Error deleting compte with ID: {}", idCompte, e);
		}
	}




}
