package com.ectimel.employeeservice.service.impl;

import com.ectimel.employeeservice.dto.ApiResponseDto;
import com.ectimel.employeeservice.dto.DepartmentDto;
import com.ectimel.employeeservice.dto.EmployeeDto;
import com.ectimel.employeeservice.dto.PaginatedResponse;
import com.ectimel.employeeservice.entity.Employee;
import com.ectimel.employeeservice.exception.EmailAlreadyExistException;
import com.ectimel.employeeservice.exception.ResourceNotFoundException;
import com.ectimel.employeeservice.repository.EmployeeRepository;
import com.ectimel.employeeservice.service.ApiClient;
import com.ectimel.employeeservice.service.EmployeeService;
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
    //    private RestTemplate restTemplate;
//    private WebClient webClient;
    private ApiClient apiClient;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper, ApiClient apiClient) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
//        this.restTemplate = restTemplate;
//        this.webClient = webClient;
        this.apiClient = apiClient;
    }


    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Optional<Employee> employeeAsOptional = employeeRepository.findByEmail(employeeDto.getEmail());
        if (employeeAsOptional.isPresent()) {
            throw new EmailAlreadyExistException(employeeDto.getEmail());
        }

        Employee employee = modelMapper.map(employeeDto, Employee.class);

        return modelMapper.map(employeeRepository.save(employee), EmployeeDto.class);
    }

    @Override
    public ApiResponseDto getEmployeeById(Long id) {

        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", String.valueOf(id)));

//        REST TEMPLATE - DON'T USE
//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity(
//                "http://localhost:8080/api/v1/department/" + employee.getDepartmentCode(),
//                DepartmentDto.class);
//        DepartmentDto departmentDto = responseEntity.getBody();


//        WEBFLUX
//        DepartmentDto departmentDto = webClient.get()
//                .uri("http://localhost:8080/api/v1/department/" + employee.getDepartmentCode())
//                .retrieve()
//                .bodyToMono(DepartmentDto.class)
//                .block();


//      SPRING CLOUD FEIGN
        DepartmentDto departmentDto = apiClient.getDepartmentByCode(employee.getDepartmentCode())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "code", employee.getDepartmentCode()));


        return new ApiResponseDto(
                modelMapper.map(employee, EmployeeDto.class),
                departmentDto);
    }

    @Override
    public PaginatedResponse<EmployeeDto> getAllEmployee(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Employee> employeesPage = employeeRepository.findAll(pageable);
        List<Employee> employeesList = employeesPage.getContent();

        List<EmployeeDto> employeesDtoList = employeesList
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .collect(Collectors.toList());

        // use <T> after . before builder() as in example below
        return PaginatedResponse.<EmployeeDto>builder()
                .content(employeesDtoList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(employeesDtoList.size())
                .last(employeesPage.isLast())
                .build();
    }

}
