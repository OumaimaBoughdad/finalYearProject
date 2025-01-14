package com.example.security_services.service;

//import com.example.compte_service.CompteServiceApplication;
import com.example.security_services.entity.Employee;
import com.example.security_services.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    @KafkaListener(topics = "employeeTopic", groupId = "grprmp")
    public void saveUser(Employee employee) {

        long id = employee.getIdEmployee();
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        String email = employee.getEmail();
        String phoneNumber = employee.getPhoneNumber();
        String role = employee.getRole();

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        String password = employee.getPassword();

        //String passwordEncoded = passwordEncoder.encode(password);



        String sql ="INSERT INTO employees(id_employee,email,first_name, last_name, password, phone_number, role) VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, id,email, firstName, lastName, password, phoneNumber, role);




    }

    @KafkaListener(topics = "employeeupdat", groupId = "grprmp")
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

    @KafkaListener(topics = "employeedelet", groupId = "grprmp")
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

    public String generateToken(String username) {

        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }


}
