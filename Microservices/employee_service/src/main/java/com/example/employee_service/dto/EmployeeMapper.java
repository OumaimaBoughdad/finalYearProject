package com.example.employee_service.dto;

import com.example.employee_service.entity.Employee;


public class EmployeeMapper {
    public static Employee toEntity(EmployeeDTO dto) {
        return new Employee(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getRole(),
                "defaultPassword" // Si vous voulez gérer un mot de passe par défaut
        );
    }

    public static EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setRole(employee.getRole());
        return dto;
    }
}