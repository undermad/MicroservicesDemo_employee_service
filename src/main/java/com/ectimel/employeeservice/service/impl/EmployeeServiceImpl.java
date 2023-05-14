package com.ectimel.employeeservice.service.impl;

import com.ectimel.employeeservice.dto.EmployeeDto;
import com.ectimel.employeeservice.dto.EmployeesResponse;
import com.ectimel.employeeservice.entity.Employee;
import com.ectimel.employeeservice.exception.EmailAlreadyExistException;
import com.ectimel.employeeservice.exception.ResourceNotFoundException;
import com.ectimel.employeeservice.repository.EmployeeRepository;
import com.ectimel.employeeservice.service.EmployeeService;
import lombok.Builder;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Optional<Employee> employeeAsOptional = employeeRepository.findByEmail(employeeDto.getEmail());
        if (employeeAsOptional.isPresent()) {
            throw new EmailAlreadyExistException(employeeDto.getEmail());
        }

        Employee employee = modelMapper.map(employeeDto, Employee.class);

        return modelMapper.map(employeeRepository.save(employee), EmployeeDto.class);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {

        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeesResponse getAllEmployee(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Employee> employeesPage = employeeRepository.findAll(pageable);
        List<Employee> employeesList = employeesPage.getContent();

        List<EmployeeDto> employeesDtoList = employeesList
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();

        return EmployeesResponse.builder()
                .content(employeesDtoList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(employeesDtoList.size())
                .last(employeesPage.isLast())
                .build();
    }

}
