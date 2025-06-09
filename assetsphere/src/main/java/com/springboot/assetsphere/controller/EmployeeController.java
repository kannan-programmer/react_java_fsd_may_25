package com.springboot.assetsphere.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.assetsphere.dto.EmployeeDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        logger.info("Adding new employee: " + employee.getUser().getUsername());
        Employee savedEmployee = employeeService.addEmployee(employee);
        logger.info("Employee added with ID: " + savedEmployee.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployee(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all employees, page: " + page + ", size: " + size);
        return ResponseEntity.ok(employeeService.getAllEmployee(page, size));
    }

    @GetMapping("/getBy-Id/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) throws ResourceNotFoundException {
        logger.info("Fetching employee by ID: " + id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching employees by username: " + username);
        return ResponseEntity.ok(employeeService.getEmployeesByUsername(username, page, size));
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching employees by email: " + email);
        return ResponseEntity.ok(employeeService.getEmployeesByEmail(email, page, size));
    }

    @GetMapping("/by-jobtitle/{jobTitle}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByJobTitle(
            @PathVariable String jobTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching employees by job title: " + jobTitle);
        return ResponseEntity.ok(employeeService.getEmployeesByJobTitle(jobTitle, page, size));
    }
}