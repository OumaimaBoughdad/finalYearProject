package com.example.employee_service.dto;

import com.example.employee_service.entity.Employee;


public class EmployeeMapper {
    public static Employee toEntity(EmployeeDTO dto) {
        return new Employee(
                dto.getIdEmployee(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getRole(),
                "defaultPassword"
        );
    }

    public static EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setIdEmployee(employee.getIdEmployee());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setRole(employee.getRole());
        return dto;
    }
}
