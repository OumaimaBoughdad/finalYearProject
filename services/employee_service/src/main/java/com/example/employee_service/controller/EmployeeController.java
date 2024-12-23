package com.example.employee_service.controller;

import com.example.employee_service.dto.EmployeeDTO;
import com.example.employee_service.dto.EmployeeMapper;
import com.example.employee_service.entity.Employee;
import com.example.employee_service.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create a new employee
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);

        return ResponseEntity.ok(createdEmployee);
    }

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
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(EmployeeMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update an existing employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeDTO employeeDetails) {
        Employee updatedEmployee = employeeService.updateEmployee(id, EmployeeMapper.toEntity(employeeDetails));
        return ResponseEntity.ok(EmployeeMapper.toDTO(updatedEmployee));
    }

    // Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmail(@PathVariable String email) {
        Employee employee = employeeService.findByEmail(email);
        return ResponseEntity.ok(EmployeeMapper.toDTO(employee));
    }
}
