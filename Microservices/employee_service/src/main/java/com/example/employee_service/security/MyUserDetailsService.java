package com.example.employee_service.security;



import com.example.employee_service.entity.Employee;
import com.example.employee_service.reposetory.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public MyUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String role = employee.getRole().startsWith("ROLE_") ? employee.getRole() : "ROLE_" + employee.getRole();
        return new User(
                employee.getEmail(),
                employee.getPassword(),
                Collections.singleton(() -> role)
        );
    }

}

