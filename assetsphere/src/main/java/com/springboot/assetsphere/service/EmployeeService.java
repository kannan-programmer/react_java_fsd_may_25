package com.springboot.assetsphere.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.EmployeeDTO;
import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepo;
    private final UserService userService;
    private final EmployeeDTO employeeDTOConverter;

    public EmployeeService(EmployeeRepository employeeRepo,
                           UserService userService,
                           EmployeeDTO employeeDTOConverter) {
        this.employeeRepo = employeeRepo;
        this.userService = userService;
        this.employeeDTOConverter = employeeDTOConverter;
    }

    public Employee addEmployee(Employee employee) {
        logger.info("Adding a new employee");

        User userInput = employee.getUser();
        if (userInput == null || userInput.getUsername() == null) {
            logger.error("User or username is null in addEmployee");
            throw new IllegalArgumentException("User input or username cannot be null");
        }

        User user;
        try {
            user = Optional.ofNullable(userService.getUserByUsername(userInput.getUsername()))
                    .orElseThrow(() -> new IllegalArgumentException("User does not exist with username: " + userInput.getUsername()));
        } catch (IllegalArgumentException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        }

        employee.setUser(user);
        employee.setCreatedAt(LocalDate.now());

        Employee savedEmployee = employeeRepo.save(employee);
        logger.info("Employee saved successfully with id {}", savedEmployee.getId());

        return savedEmployee;
    }

    public List<EmployeeDTO> getAllEmployee(int page, int size) {
        logger.info("Fetching all employees: page {}, size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        List<Employee> employeeList = employeeRepo.findAll(pageable).getContent();
        return employeeDTOConverter.convertEmployeeToDto(employeeList);
    }

    public EmployeeDTO getEmployeeById(int id) throws ResourceNotFoundException {
        logger.info("Fetching employee by id: {}", id);
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id {}", id);
                    return new ResourceNotFoundException("Employee Not Found");
                });
        return employeeDTOConverter.convertEmployeeToDto(List.of(employee)).get(0);
    }

    public List<EmployeeDTO> getEmployeesByEmail(String email, int page, int size) {
        logger.info("Fetching employees by email: {}, page: {}, size: {}", email, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return employeeDTOConverter.convertEmployeeToDto(employeeRepo.findByEmail(email, pageable).getContent());
    }

    public List<EmployeeDTO> getEmployeesByUsername(String username, int page, int size) {
        logger.info("Fetching employees by username: {}, page: {}, size: {}", username, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return employeeDTOConverter.convertEmployeeToDto(employeeRepo.findByUsername(username, pageable).getContent());
    }

    public List<EmployeeDTO> getEmployeesByJobTitle(String jobTitle, int page, int size) {
        logger.info("Fetching employees by job title: {}, page: {}, size: {}", jobTitle, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return employeeDTOConverter.convertEmployeeToDto(employeeRepo.findByJobTitle(jobTitle, pageable).getContent());
    }
}