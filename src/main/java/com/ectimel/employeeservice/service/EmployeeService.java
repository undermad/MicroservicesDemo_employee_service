package com.ectimel.employeeservice.service;

import com.ectimel.employeeservice.dto.EmployeeDto;
import com.ectimel.employeeservice.dto.EmployeesResponse;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto getEmployeeById(Long id);
    EmployeesResponse getAllEmployee(int pageNo, int pageSize, String sortBy, String sortDir);
}
