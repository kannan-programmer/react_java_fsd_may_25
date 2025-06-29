package com.springboot.assetsphere.controller;

import java.io.IOException;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.assetsphere.dto.EmployeeDTO;
import com.springboot.assetsphere.dto.EmployeeDashboardStatsDto;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) throws ResourceNotFoundException, EmployeeNotFoundException {
        logger.info("Fetching employee by ID: " + id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // Removed pagination here, assuming not needed for getByJobTitle
    @GetMapping("/by-jobtitle/{jobTitle}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByJobTitle(@PathVariable String jobTitle) {
        logger.info("Fetching employees by job title: " + jobTitle);
        return ResponseEntity.ok(employeeService.getEmployeesByJobTitle(jobTitle));
    }

    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    @GetMapping("/by-username/{username}")
    public ResponseEntity<EmployeeDTO> getEmployeeByUsername(@PathVariable String username) throws ResourceNotFoundException, EmployeeNotFoundException {
        logger.info("Fetching employee by username: " + username);
        return ResponseEntity.ok(employeeService.getEmployeeByUsername(username));
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<Employee> updateEmployeeByUsername(@PathVariable String username, @RequestBody Employee updatedEmployee) throws ResourceNotFoundException, EmployeeNotFoundException {
        logger.info("Updating employee by username: {}", username);
        return ResponseEntity.ok(employeeService.updateEmployeeByUsername(username, updatedEmployee));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteEmployeeByUsername(@PathVariable String username) throws ResourceNotFoundException, EmployeeNotFoundException {
        logger.info("Deleting employee by username: {}", username);
        employeeService.deleteEmployeeByUsername(username);
        return ResponseEntity.ok().body("Employee deleted successfully for username: " + username);
    }

    @PostMapping("/upload-image/profilepic")
    public Employee uploadpic(Principal principal, @RequestParam("file") MultipartFile file) throws IOException, ResourceNotFoundException {
        logger.info("Uploading profile picture for user: {}", principal.getName());
        return employeeService.uploadProfilePic(file, principal.getName());
    }
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<EmployeeDashboardStatsDto> getDashboardStats(Principal principal) {
        try {
            EmployeeDashboardStatsDto stats = employeeService.getDashboardStats(principal.getName());
            return ResponseEntity.ok(stats);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
