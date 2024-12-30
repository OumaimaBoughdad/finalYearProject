package com.example.security_services.service;

import com.example.security_services.entity.Employee;
import com.example.security_services.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    @KafkaListener(topics = "employeeTopic", groupId = "groupG")
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

    public String generateToken(String username) {

        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }


}
