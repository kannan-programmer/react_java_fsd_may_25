package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.EmployeeDTO;
import com.springboot.assetsphere.dto.EmployeeDashboardStatsDto;
import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.enums.ServiceStatus;
import com.springboot.assetsphere.enums.Status;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.*;
import com.springboot.assetsphere.service.EmployeeService;
import com.springboot.assetsphere.service.UserService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepo;
    @Mock private UserService userService;
    @Mock private EmployeeDTO employeeDTOConverter; // Mocks the DTO converter
    @Mock private MultipartFile multipartFile;

    // Mocks for Dashboard Stats
    @Mock private AssetAllocationRepository allocationRepo;
    @Mock private ServiceRequestRepository serviceRequestRepo;
    @Mock private AssetAuditRepository assetAuditRepo;
    @Mock private AssetRepository assetRepo; 
    @Mock private AssetRequestRepository assetRequestRepo; 
    @Mock private LiquidAssetRequestRepository liquidAssetRepo;


    @InjectMocks
    private EmployeeService employeeService;

    private AutoCloseable closeable; 

    private User user;
    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("john123");
        user.setRole(Role.EMPLOYEE);
        user.setStatus(Status.ACTIVE);

        employee = new Employee();
        employee.setId(1);
        employee.setUser(user);
        employee.setName("John Doe"); // Added name for comprehensive DTO testing
        employee.setJobTitle("Software Engineer");
        employee.setContactNumber("1234567890");
        employee.setAddress("123 Main St");
        employee.setCreatedAt(LocalDate.now());

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1);
        employeeDTO.setUsername("john123");
        employeeDTO.setName("John Doe");
        employeeDTO.setJobTitle("Software Engineer");
        employeeDTO.setContactNumber("1234567890");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setStatus(Status.ACTIVE);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
       
        user = null;
        employee = null;
        employeeDTO = null;
    }

  
    @Test
    void testAddEmployee_Success() {
     
        when(userService.getUserByUsername("john123")).thenReturn(user);
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        Employee inputEmployee = new Employee();
        inputEmployee.setUser(user); // Ensure the input has a user
        inputEmployee.setJobTitle("New Job"); // Example additional field

        Employee result = employeeService.addEmployee(inputEmployee);

        assertNotNull(result);
        assertEquals("john123", result.getUser().getUsername());
        assertEquals(Role.EMPLOYEE, result.getUser().getRole());
        assertEquals(Status.ACTIVE, result.getUser().getStatus());
        assertEquals(LocalDate.now(), result.getCreatedAt()); // Verify createdAt is set

        verify(userService, times(1)).getUserByUsername("john123");
        verify(employeeRepo, times(1)).save(inputEmployee); // Verify save was called on the input object
    }

    @Test
    void testAddEmployee_NullUser_ThrowsException() {
        Employee nullUserEmployee = new Employee();
        nullUserEmployee.setUser(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee(nullUserEmployee);
        });

        assertEquals("User input or username cannot be null", ex.getMessage());
        verifyNoInteractions(userService);
        verifyNoInteractions(employeeRepo);
    }

    @Test
    void testAddEmployee_NullUsername_ThrowsException() {
        User noUsernameUser = new User(); 
        Employee badEmployee = new Employee();
        badEmployee.setUser(noUsernameUser);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee(badEmployee);
        });

        assertEquals("User input or username cannot be null", ex.getMessage());
        verifyNoInteractions(userService);
        verifyNoInteractions(employeeRepo);
    }

    @Test
    void testAddEmployee_UserNotFound_ThrowsException() {
        when(userService.getUserByUsername("john123")).thenReturn(null);
        Employee emp = new Employee();
        emp.setUser(user);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.addEmployee(emp);
        });

        assertEquals("User does not exist with username: john123", ex.getMessage());
        verify(userService, times(1)).getUserByUsername("john123");
        verifyNoInteractions(employeeRepo);
    }

    @Test
    void testGetEmployeeById_Success() throws ResourceNotFoundException, EmployeeNotFoundException {
        
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(employeeDTOConverter.convertEmployeeToDto(List.of(employee))).thenReturn(List.of(employeeDTO));

        EmployeeDTO result = employeeService.getEmployeeById(1);

        assertNotNull(result);
        assertEquals("john123", result.getUsername());
        assertEquals("John Doe", result.getName());
        assertEquals(1, result.getId());

        verify(employeeRepo, times(1)).findById(1);
        verify(employeeDTOConverter, times(1)).convertEmployeeToDto(List.of(employee));
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepo.findById(99)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeById(99);
        });
        assertEquals("Employee Not Found", ex.getMessage());
        verify(employeeRepo, times(1)).findById(99);
        verifyNoInteractions(employeeDTOConverter);
    }

    //  Test getEmployeeByUsername (Crucial method for retrieval) ---
    @Test
    void testGetEmployeeByUsername_Success() throws ResourceNotFoundException, EmployeeNotFoundException {
        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.of(employee));
        when(employeeDTOConverter.convertEmployeeToDto(employee)).thenReturn(employeeDTO);

        // Act
        EmployeeDTO result = employeeService.getEmployeeByUsername("john123");

        // Assert
        assertNotNull(result);
        assertEquals("john123", result.getUsername());
        assertEquals("John Doe", result.getName());
        assertEquals(1, result.getId());

    }

    @Test
    void testGetEmployeeByUsername_NotFound() {
        
        when(employeeRepo.findByUserUsername("ghost")).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeByUsername("ghost");
        });
        assertEquals("Employee not found for username: ghost", ex.getMessage());
        verify(employeeRepo, times(1)).findByUserUsername("ghost");
        verifyNoInteractions(employeeDTOConverter);
    }

    //  Test updateEmployeeByUsername (Crucial method for updates) ---
    @Test
    void testUpdateEmployeeByUsername_Success_PartialUpdate() throws ResourceNotFoundException, EmployeeNotFoundException {
        // Arrange
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1);
        existingEmployee.setUser(user); 
        existingEmployee.setName("Old Name");
        existingEmployee.setJobTitle("Old Title");
        existingEmployee.setAddress("Old Address");

        Employee updatedInfo = new Employee();
        updatedInfo.setName("Updated Name"); 
        updatedInfo.setAddress("New Address"); 

        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.of(existingEmployee));
        when(employeeRepo.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved entity

        // Act
        Employee result = employeeService.updateEmployeeByUsername("john123", updatedInfo);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Old Title", result.getJobTitle()); // Should remain unchanged
        assertEquals("New Address", result.getAddress()); // Should be updated
}

    @Test
    void testUpdateEmployeeByUsername_NotFound() {
       
        when(employeeRepo.findByUserUsername("ghost")).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployeeByUsername("ghost", new Employee());
        });
        assertEquals("Employee not found for username: ghost", ex.getMessage());
        
    }

    // --- 5. Test deleteEmployeeByUsername (Crucial method for deletion) ---
    @Test
    void testDeleteEmployeeByUsername_Success() throws ResourceNotFoundException, EmployeeNotFoundException {
      
        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepo).delete(employee);

        employeeService.deleteEmployeeByUsername("john123");

       
    }

    @Test
    void testDeleteEmployeeByUsername_NotFound() {
        
        when(employeeRepo.findByUserUsername("unknown")).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteEmployeeByUsername("unknown");
        });
        assertEquals("Employee not found for username: unknown", ex.getMessage());
        verify(employeeRepo, times(1)).findByUserUsername("unknown");
        verify(employeeRepo, never()).delete(any(Employee.class));
    }

    @Test
    void testUploadProfilePic_Success() throws IOException, ResourceNotFoundException {
        // Arrange
        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.of(employee));
        when(multipartFile.getOriginalFilename()).thenReturn("profile.jpg");
        when(multipartFile.getSize()).thenReturn(1024L); // 1 KB
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));

        

        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee result = employeeService.uploadProfilePic(multipartFile, "john123");

        // Assert
        assertNotNull(result);
        assertEquals("profile.jpg", result.getImageUrl());
         }

    @Test
    void testUploadProfilePic_EmployeeNotFound() {
        // Arrange
        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                employeeService.uploadProfilePic(multipartFile, "john123"));

        assertEquals("Employee not found for username: john123", ex.getMessage());
        
    }

    @Test
    void testUploadProfilePic_InvalidFileName() {
        
        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.of(employee));
        when(multipartFile.getOriginalFilename()).thenReturn("profile"); // No dot or extension

    
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                employeeService.uploadProfilePic(multipartFile, "john123"));

        assertTrue(ex.getMessage().contains("Invalid file name"));
       
    }

    @Test
    void testUploadProfilePic_UnsupportedExtension() {
        
        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.of(employee));
        when(multipartFile.getOriginalFilename()).thenReturn("profile.txt");

    
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                employeeService.uploadProfilePic(multipartFile, "john123"));

        assertTrue(ex.getMessage().contains("File Extension txt not allowed"));
        
    }

    @Test
    void testUploadProfilePic_Oversize() {
        // Arrange
        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.of(employee));
        when(multipartFile.getOriginalFilename()).thenReturn("profile.jpg");
        when(multipartFile.getSize()).thenReturn(4_000_000L); // > 3000 KB (3MB)

        
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                employeeService.uploadProfilePic(multipartFile, "john123"));

        assertTrue(ex.getMessage().contains("Image Oversized"));
       
    }

    //  Test getDashboardStats (Crucial for dashboard data aggregation) 
    @Test
    void testGetDashboardStats_Success() throws ResourceNotFoundException {
        
        when(employeeRepo.findByUserUsername("john123")).thenReturn(Optional.of(employee));
        when(allocationRepo.countByEmployeeId(employee.getId())).thenReturn(5);
        when(serviceRequestRepo.countByEmployeeId(employee.getId())).thenReturn(3);
        when(assetAuditRepo.countByEmployeeId(employee.getId())).thenReturn(2);
        when(serviceRequestRepo.countByEmployeeIdAndStatus(employee.getId(), ServiceStatus.IN_PROGRESS)).thenReturn(1);
        when(allocationRepo.countByEmployeeIdAndStatus(employee.getId(), AllocationStatus.ASSIGNED)).thenReturn(4);

        
        EmployeeDashboardStatsDto result = employeeService.getDashboardStats("john123");

        // Assert
        assertNotNull(result);
        assertEquals(List.of(
            "Assets Allocated",
            "Service Requests Raised",
            "Audits Submitted",
            "Pending Requests",
            "Active Allocations"
        ), result.getCategories());
        assertEquals(List.of(5, 3, 2, 1, 4), result.getCounts());

    }

    @Test
    void testGetDashboardStats_EmployeeNotFound() {
        
        when(employeeRepo.findByUserUsername("unknown@example.com")).thenReturn(Optional.empty());

       
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                employeeService.getDashboardStats("unknown@example.com"));

        assertEquals("Employee not found for username: unknown@example.com", ex.getMessage());
     
    }
}