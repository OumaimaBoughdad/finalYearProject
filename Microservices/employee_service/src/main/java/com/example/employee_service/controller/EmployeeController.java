package com.example.employee_service.controller;

import com.example.employee_service.dto.EmployeeDTO;
import com.example.employee_service.dto.EmployeeMapper;
import com.example.employee_service.entity.Employee;
import com.example.employee_service.reposetory.EmployeeRepository;
import com.example.employee_service.service.EmployeeService;
import com.example.employee_service.service.EmployeeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    Logger logger = Logger.getLogger(EmployeeController.class.getName());


    private final EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create a new employee
//    @PostMapping
//    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
//        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
//
//        return ResponseEntity.ok(createdEmployee);
//    }

    // Get all employees
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees()
                .stream()
                .map(EmployeeMapper::toDTO)
                .toList();
        return ResponseEntity.ok(employees);
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + id));
        return ResponseEntity.ok(employee);
    }


    // Update an existing employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeDTO employeeDetails) {
        Employee updatedEmployee = employeeService.updateEmployee(id, EmployeeMapper.toEntity(employeeDetails));
        if (updatedEmployee != null) {
            employeeService.sendEmployeeforupdate(updatedEmployee);
            logger.info("employee was sent to be update "+updatedEmployee.getIdEmployee());
        }
        return ResponseEntity.ok(EmployeeMapper.toDTO(updatedEmployee));
    }

    // Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeByIdOrThrow(id);
        if (employee != null) {
            employeeService.sendEmployeedelet(employee);
            logger.info("employee was sent to be deleted "+ employee.getIdEmployee());
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-email")
    public ResponseEntity<Employee> getEmployeeByEmail(@RequestParam String email) {
        Employee employee = employeeService.findByEmail(email);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    // publish the employee object

    @PostMapping
    public ResponseEntity<Employee> sendJsonMessage(@RequestBody Employee employee){
        employeeService.createnewEmployee(employee);
        employeeService.sendEmployee(employee);
        return ResponseEntity.ok(employee);
    }

}
