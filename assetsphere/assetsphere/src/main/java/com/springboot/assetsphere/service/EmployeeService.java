package com.springboot.assetsphere.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.assetsphere.dto.EmployeeDTO;
import com.springboot.assetsphere.dto.EmployeeDashboardStatsDto;
import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.enums.ServiceStatus;
import com.springboot.assetsphere.enums.Status;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetAuditRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.LiquidAssetRequestRepository;
import com.springboot.assetsphere.repository.ServiceRequestRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepo;
    private final UserService userService;
    private final EmployeeDTO employeeDTOConverter;
    private AssetRepository assetRepo;
    private AssetRequestRepository assetRequestRepo;
    private AssetAllocationRepository allocationRepo;
    
    private ServiceRequestRepository serviceRequestRepo;
    private LiquidAssetRequestRepository liquidAssetRepo;
    private AssetAuditRepository assetAuditRepo;

    

 

	public EmployeeService(EmployeeRepository employeeRepo, UserService userService, EmployeeDTO employeeDTOConverter,
			AssetRepository assetRepo, AssetRequestRepository assetRequestRepo,
			AssetAllocationRepository allocationRepo, ServiceRequestRepository serviceRequestRepo,
			LiquidAssetRequestRepository liquidAssetRepo, AssetAuditRepository assetAuditRepo) {
		super();
		this.employeeRepo = employeeRepo;
		this.userService = userService;
		this.employeeDTOConverter = employeeDTOConverter;
		this.assetRepo = assetRepo;
		this.assetRequestRepo = assetRequestRepo;
		this.allocationRepo = allocationRepo;
		this.serviceRequestRepo = serviceRequestRepo;
		this.liquidAssetRepo = liquidAssetRepo;
		this.assetAuditRepo = assetAuditRepo;
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
        user.setRole(Role.EMPLOYEE);
        user.setStatus(Status.ACTIVE);
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

    public EmployeeDTO getEmployeeById(int id) throws  EmployeeNotFoundException {
        logger.info("Fetching employee by id: {}", id);
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id {}", id);
                    return new EmployeeNotFoundException("Employee Not Found");
                });
        return employeeDTOConverter.convertEmployeeToDto(List.of(employee)).get(0);
    }

    public EmployeeDTO getEmployeeByUsername(String username) throws  EmployeeNotFoundException {
        logger.info("Fetching employee by username: {}", username);
        
        Employee employee = employeeRepo.findByUserUsername(username)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for username: " + username));
        
        return employeeDTOConverter.convertEmployeeToDto(employee);
    }
    
    
    
    public List<EmployeeDTO> getEmployeesByJobTitle(String jobTitle) {
        logger.info("Fetching employees by job title: {}", jobTitle);
        return employeeDTOConverter.convertEmployeeToDto(employeeRepo.findByJobTitle(jobTitle));
    }
    
    
    
    @Transactional
    public Employee updateEmployeeByUsername(String username, Employee updatedEmployee) throws  EmployeeNotFoundException {
        Employee existing = employeeRepo.findByUserUsername(username)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for username: " + username));

        if (updatedEmployee.getName() != null)
            existing.setName(updatedEmployee.getName());
        if (updatedEmployee.getAddress() != null)
            existing.setAddress(updatedEmployee.getAddress());
        if (updatedEmployee.getContactNumber() != null)
            existing.setContactNumber(updatedEmployee.getContactNumber());
        if (updatedEmployee.getGender() != null)
            existing.setGender(updatedEmployee.getGender());
        if (updatedEmployee.getImageUrl() != null)
            existing.setImageUrl(updatedEmployee.getImageUrl());
        if (updatedEmployee.getJobTitle() != null)
            existing.setJobTitle(updatedEmployee.getJobTitle());

        return employeeRepo.save(existing);
    }

    @Transactional
    public void deleteEmployeeByUsername(String username) throws  EmployeeNotFoundException {
        Employee employee = employeeRepo.findByUserUsername(username)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for username: " + username));
        employeeRepo.delete(employee);
    }

    
    
    public Employee uploadProfilePic(MultipartFile file, String username) throws IOException, ResourceNotFoundException {
        Employee employee = employeeRepo.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for username: " + username));
        logger.info("Uploading profile picture for employee: " + employee.getName());

        String originalFileName = file.getOriginalFilename();
        logger.info("Original filename: " + originalFileName);

        if (originalFileName == null || !originalFileName.contains(".")) {
            logger.error("Invalid file name");
            throw new RuntimeException("Invalid file name");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        if (!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
            logger.error("File extension not allowed: " + extension);
            throw new RuntimeException("File Extension " + extension + " not allowed. Allowed Extensions: jpg, jpeg, png, gif, svg");
        }
        logger.info("File extension approved: " + extension);

        long kbs = file.getSize() / 1024;
        if (kbs > 3000) {
            logger.error("File size too large: " + kbs + " KB");
            throw new RuntimeException("Image Oversized. Max allowed size is 3000 KB");
        }
        logger.info("Profile image size: " + kbs + " KB");

        String uploadFolder = "C:\\Users\\GOPALAKANNAN_N\\asset-sphere-ui\\public\\images";
        Files.createDirectories(Path.of(uploadFolder));
        logger.info("Directory ready: " + uploadFolder);

        Path path = Paths.get(uploadFolder, originalFileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        employee.setImageUrl(originalFileName);
        return employeeRepo.save(employee);
    }
    
    public EmployeeDashboardStatsDto getDashboardStats(String username) throws ResourceNotFoundException {
        Employee employee = employeeRepo.findByUserUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found for username: " + username));

        EmployeeDashboardStatsDto dto = new EmployeeDashboardStatsDto();

        List<String> labels = List.of(
            "Assets Allocated",
            "Service Requests Raised",
            "Audits Submitted",
            "Pending Requests",
            "Active Allocations"
        );

        int allocatedAssets = allocationRepo.countByEmployeeId(employee.getId());
        int serviceRequests = serviceRequestRepo.countByEmployeeId(employee.getId());
        int audits = assetAuditRepo.countByEmployeeId(employee.getId());
        int pending = serviceRequestRepo.countByEmployeeIdAndStatus(employee.getId(), ServiceStatus.IN_PROGRESS);
        int activeAllocations = allocationRepo.countByEmployeeIdAndStatus(employee.getId(), AllocationStatus.ASSIGNED);

        List<Integer> counts = List.of(
            allocatedAssets,
            serviceRequests,
            audits,
            pending,
            activeAllocations
        );

        dto.setCategories(labels);
        dto.setCounts(counts);

        return dto;
    }


}
