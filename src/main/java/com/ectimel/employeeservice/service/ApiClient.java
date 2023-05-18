package com.ectimel.employeeservice.service;

import com.ectimel.employeeservice.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(url = "http://localhost:8080", value = "department-service")
public interface ApiClient {

    @GetMapping("api/v1/department/{code}")
    Optional<DepartmentDto> getDepartmentByCode(@PathVariable String code);

}
