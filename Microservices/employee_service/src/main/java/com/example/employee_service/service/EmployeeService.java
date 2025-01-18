package com.example.employee_service.service;



import com.example.employee_service.dto.EmployeeDTO;
import com.example.employee_service.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employeeDetails);
    void deleteEmployee(Long id);
    Employee findByEmail(String email);
    void sendEmployee(Employee employee);
    Employee createnewEmployee(Employee employee);
    void sendEmployeedelet(Employee employee);
    public Employee getEmployeeByIdOrThrow(Long id);
    public void sendEmployeeforupdate(Employee employee);
    List<Employee> findByLastName(String lastName);

}
