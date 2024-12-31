package com.example.transaction_service.reposetory;

import com.example.transaction_service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository  extends JpaRepository<Employee, Long > {


}
