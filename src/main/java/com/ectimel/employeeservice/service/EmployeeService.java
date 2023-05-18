package com.ectimel.employeeservice.service;

import com.ectimel.employeeservice.dto.ApiResponseDto;
import com.ectimel.employeeservice.dto.EmployeeDto;
import com.ectimel.employeeservice.dto.PaginatedResponse;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    ApiResponseDto getEmployeeById(Long id);
    PaginatedResponse getAllEmployee(int pageNo, int pageSize, String sortBy, String sortDir);
}
