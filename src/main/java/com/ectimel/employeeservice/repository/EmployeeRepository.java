package com.ectimel.employeeservice.repository;

import com.ectimel.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
