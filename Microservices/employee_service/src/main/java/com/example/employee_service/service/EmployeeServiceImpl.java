package com.example.employee_service.service;

import com.example.employee_service.dto.EmployeeDTO;
import com.example.employee_service.dto.EmployeeMapper;
import com.example.employee_service.entity.Employee;
import com.example.employee_service.reposetory.EmployeeRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String , Employee> kafkaTemplate;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, KafkaTemplate<String , Employee> kafkaTemplate) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder; // Initialisation correcte
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Crypter le mot de passe de l'employé
        String encryptedPassword = passwordEncoder.encode(employeeDTO.getPassword());

        // Mapper EmployeeDTO to Employee et définir le mot de passe crypté
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        employee.setPassword(encryptedPassword);

        // Sauvegarder l'employé dans la base de données
        Employee savedEmployee = employeeRepository.save(employee);

        // Retourner l'EmployeeDTO avec les informations de l'employé créé
        return EmployeeMapper.toDTO(savedEmployee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee getEmployeeByIdOrThrow(Long id) {
        return getEmployeeById(id)
                .orElseThrow(() -> new RuntimeException());
    }


    @Override
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setRole(employeeDetails.getRole());
        employee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));

        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee findByEmail(String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        return employee.orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
    }


    // function to send the employee object to the kafka broker
    public void sendEmployee(Employee employee) {
        Message<Employee> message = MessageBuilder
                .withPayload(employee)
                .setHeader(KafkaHeaders.TOPIC, "employeeTopic")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendEmployeedelet(Employee employee) {
        Message<Employee> message = MessageBuilder
                .withPayload(employee)
                .setHeader(KafkaHeaders.TOPIC, "employeedelet")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendEmployeeforupdate(Employee employee) {
        Message<Employee> message = MessageBuilder
                .withPayload(employee)
                .setHeader(KafkaHeaders.TOPIC, "employeeupdat")
                .build();
        kafkaTemplate.send(message);
    }

    @Override
    public Employee createnewEmployee(Employee employee) {
       return employeeRepository.save(employee);
    }

}

