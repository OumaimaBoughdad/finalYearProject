package clients.clients_service;

import clients.clients_service.entity.Client;
import clients.clients_service.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientsServiceApplication {

	Logger log = LoggerFactory.getLogger(ClientsServiceApplication.class);
	private final JdbcTemplate jdbcTemplate;

	public ClientsServiceApplication( JdbcTemplate jdbcTemplate) {

		this.jdbcTemplate = jdbcTemplate;
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(ClientsServiceApplication.class, args);
	}


	// consume the employee object
	@KafkaListener(topics = "employeeTopic", groupId = "groupEm")
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

	@KafkaListener(topics = "tpc", groupId = "groupEm")
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
	@KafkaListener(topics = "employeedelet", groupId = "groupEm")
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

	@KafkaListener(topics = "employeeupdat", groupId = "groupEm")
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


}
