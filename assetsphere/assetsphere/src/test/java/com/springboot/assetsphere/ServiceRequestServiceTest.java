package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.ServiceRequestDTO;
import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.enums.ServiceStatus;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.ServiceRequest;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.ServiceRequestRepository;
import com.springboot.assetsphere.service.ServiceRequestService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks; // REMOVED THIS IMPORT
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy; // Import Spy
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ServiceRequestServiceTest {

    @Mock
    private ServiceRequestRepository serviceRequestRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private AssetRepository assetRepo;

    @Spy // Use @Spy for ServiceRequestDTO to allow real method calls
    private ServiceRequestDTO serviceRequestDTO;

    // REMOVED @InjectMocks here, we'll instantiate manually
    private ServiceRequestService serviceRequestService;

    private AutoCloseable closeable;

    private Employee employee;
    private Asset asset;
    private AssetCategory category;
    private ServiceRequest request;
    private ServiceRequest savedRequest;
    private ServiceRequestDTO expectedDto; // Renamed for clarity

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        // Manually instantiate ServiceRequestService, passing the mocks and the spy
        // This ensures all dependencies are correctly set.
        serviceRequestService = new ServiceRequestService(serviceRequestRepo, employeeRepo, assetRepo, serviceRequestDTO);


        User user = new User();
        user.setUsername("gopal@example.com");

        employee = new Employee();
        employee.setId(1);
        employee.setName("Gopal");
        employee.setJobTitle("Developer");
        employee.setImageUrl("gopal.jpg");
        employee.setUser(user);

        category = new AssetCategory();
        category.setName("Electronics");

        asset = new Asset();
        asset.setId(100);
        asset.setAssetName("Monitor");
        asset.setImageUrl("monitor.jpg");
        asset.setModel("Dell 24");
        asset.setCategory(category);
        asset.setStatus(AssetStatus.ASSIGNED); // Set initial status

        request = new ServiceRequest();
        request.setDescription("Fix Monitor");
        request.setIssueType("Display Issue");

        savedRequest = new ServiceRequest();
        savedRequest.setId(500);
        savedRequest.setDescription("Fix Monitor");
        savedRequest.setIssueType("Display Issue");
        savedRequest.setEmployee(employee);
        savedRequest.setAsset(asset);
        savedRequest.setStatus(ServiceStatus.IN_PROGRESS);
        savedRequest.setRequestedAt(LocalDateTime.now());

        expectedDto = new ServiceRequestDTO();
        expectedDto.setDescription("Fix Monitor");
        expectedDto.setStatus("IN_PROGRESS");
        expectedDto.setUsername("gopal@example.com");
        expectedDto.setAssetId(asset.getId());
        expectedDto.setAssetName(asset.getAssetName());
        expectedDto.setAssetCategory(category.getName());
        expectedDto.setIssueType(request.getIssueType());
        expectedDto.setAdminComments(savedRequest.getAdminComments());
        expectedDto.setUserFullName(employee.getName());
        expectedDto.setJobTitle(employee.getJobTitle());
        expectedDto.setUserImageUrl(employee.getImageUrl());
        expectedDto.setAssetImageUrl(asset.getImageUrl());
        expectedDto.setAssetModel(asset.getModel());
        expectedDto.setRequestedAt(savedRequest.getRequestedAt());
        expectedDto.setResolvedAt(savedRequest.getResolvedAt());
        expectedDto.setId(savedRequest.getId());
    }

    @Test
    void testSubmitServiceRequest_Success() throws ResourceNotFoundException, AssetNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(100)).thenReturn(Optional.of(asset));
        when(serviceRequestRepo.save(any(ServiceRequest.class))).thenReturn(savedRequest);

        ServiceRequest result = serviceRequestService.submitServiceRequest(1, 100, request);

        assertNotNull(result);
        assertEquals("Fix Monitor", result.getDescription());
        assertEquals(ServiceStatus.IN_PROGRESS, result.getStatus());
        assertEquals(employee.getId(), result.getEmployee().getId());
        assertEquals(asset.getId(), result.getAsset().getId());
        assertNotNull(result.getRequestedAt());
       
    }

    @Test
    void testSubmitServiceRequest_EmployeeNotFound() {
        when(employeeRepo.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                serviceRequestService.submitServiceRequest(99, 100, request)
        );

        assertEquals("Employee not found with ID: 99", ex.getMessage());
    }

    @Test
    void testSubmitServiceRequest_AssetNotFound() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(999)).thenReturn(Optional.empty());

        AssetNotFoundException ex = assertThrows(AssetNotFoundException.class, () ->
                serviceRequestService.submitServiceRequest(1, 999, request)
        );

        assertEquals("Asset not found with ID: 999", ex.getMessage());
    }

    @Test
    void testGetAllServiceRequests() {
        List<ServiceRequest> requests = List.of(savedRequest);
        Page<ServiceRequest> page = new PageImpl<>(requests);

        when(serviceRequestRepo.findAll(any(Pageable.class))).thenReturn(page);

        List<ServiceRequestDTO> result = serviceRequestService.getAllServiceRequests(0, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("IN_PROGRESS", result.get(0).getStatus());
        assertEquals(expectedDto.getUsername(), result.get(0).getUsername());
        assertEquals(expectedDto.getAssetName(), result.get(0).getAssetName());
    }

    @Test
    void testGetByEmployeeId() {
        List<ServiceRequest> requests = List.of(savedRequest);

        when(serviceRequestRepo.findByEmployeeId(1)).thenReturn(requests);

        List<ServiceRequestDTO> result = serviceRequestService.getByEmployeeId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Fix Monitor", result.get(0).getDescription());
        assertEquals(expectedDto.getUsername(), result.get(0).getUsername());
    }

    @Test
    void testGetByAssetId() {
        List<ServiceRequest> requests = List.of(savedRequest);

        when(serviceRequestRepo.findByAssetId(100)).thenReturn(requests);

        List<ServiceRequestDTO> result = serviceRequestService.getByAssetId(100);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDto.getAssetName(), result.get(0).getAssetName());
        assertEquals(expectedDto.getAssetModel(), result.get(0).getAssetModel());
    }

    @Test
    void testGetByStatus_Valid() throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(0, 5);
        List<ServiceRequest> requests = List.of(savedRequest);
        Page<ServiceRequest> page = new PageImpl<>(requests);

        when(serviceRequestRepo.findByStatus(ServiceStatus.IN_PROGRESS, pageable)).thenReturn(page);

        List<ServiceRequestDTO> result = serviceRequestService.getByStatus("IN_PROGRESS", 0, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("IN_PROGRESS", result.get(0).getStatus());
        assertEquals(expectedDto.getUsername(), result.get(0).getUsername());
        
    }

    @Test
    void testGetByStatus_Invalid() {
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                serviceRequestService.getByStatus("INVALID", 0, 5)
        );
        assertEquals("Invalid status: INVALID", ex.getMessage());
        
    }

    @Test
    void testGetByName() {
        List<ServiceRequest> requests = List.of(savedRequest);

        when(serviceRequestRepo.findByEmployeeName("Gopal")).thenReturn(requests);

        List<ServiceRequestDTO> result = serviceRequestService.getByName("Gopal");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDto.getUserFullName(), result.get(0).getUserFullName());
        assertEquals(expectedDto.getJobTitle(), result.get(0).getJobTitle());
       
    }

    @Test
    void testGetByUsername() {
        List<ServiceRequest> requests = List.of(savedRequest);

        when(serviceRequestRepo.findByEmployeeUserUsername("gopal@example.com")).thenReturn(requests);

        List<ServiceRequestDTO> result = serviceRequestService.getByUsername("gopal@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDto.getUsername(), result.get(0).getUsername());
        
    }

    @Test
    void testUpdateById_Success_StatusChangeToInProgress() throws ResourceNotFoundException {
        ServiceRequest updatedRequestInput = new ServiceRequest();
        updatedRequestInput.setStatus(ServiceStatus.IN_PROGRESS);
        updatedRequestInput.setDescription("Updated Description");
        updatedRequestInput.setAdminComments("Reviewed by Admin");

        ServiceRequest originalRequest = new ServiceRequest();
        originalRequest.setId(500);
        originalRequest.setDescription("Original Description");
        originalRequest.setIssueType("Original Issue");
        originalRequest.setStatus(ServiceStatus.OPEN);
        originalRequest.setAsset(asset);

        when(serviceRequestRepo.findById(500)).thenReturn(Optional.of(originalRequest));
        when(serviceRequestRepo.save(any(ServiceRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServiceRequest result = serviceRequestService.updateById(500, updatedRequestInput);

        assertNotNull(result);
        assertEquals(500, result.getId());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("Reviewed by Admin", result.getAdminComments());
        assertEquals(ServiceStatus.IN_PROGRESS, result.getStatus());
        assertEquals(AssetStatus.IN_SERVICE, result.getAsset().getStatus());
        
    }

    @Test
    void testUpdateById_Success_NoStatusChange() throws ResourceNotFoundException {
        ServiceRequest updatedRequestInput = new ServiceRequest();
        updatedRequestInput.setDescription("Only Description Update");
        updatedRequestInput.setAdminComments("No status change here");

        ServiceRequest originalRequest = new ServiceRequest();
        originalRequest.setId(500);
        originalRequest.setDescription("Original Description");
        originalRequest.setIssueType("Original Issue");
        originalRequest.setStatus(ServiceStatus.RESOLVED);
        originalRequest.setAsset(asset);

        when(serviceRequestRepo.findById(500)).thenReturn(Optional.of(originalRequest));
        when(serviceRequestRepo.save(any(ServiceRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServiceRequest result = serviceRequestService.updateById(500, updatedRequestInput);

        assertNotNull(result);
        assertEquals("Only Description Update", result.getDescription());
        assertEquals("No status change here", result.getAdminComments());
        assertEquals(ServiceStatus.RESOLVED, result.getStatus());
        assertEquals(AssetStatus.ASSIGNED, result.getAsset().getStatus());
        
    }

    @Test
    void testUpdateById_ServiceRequestNotFound() {
        ServiceRequest updatedRequest = new ServiceRequest();
        updatedRequest.setStatus(ServiceStatus.RESOLVED);

        when(serviceRequestRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                serviceRequestService.updateById(999, updatedRequest)
        );

        assertEquals("Service request not found with ID: 999", ex.getMessage());
        
    }

    @Test
    void testDeleteById_Success() throws ResourceNotFoundException {
        when(serviceRequestRepo.findById(500)).thenReturn(Optional.of(savedRequest));
        doNothing().when(serviceRequestRepo).delete(savedRequest);

        serviceRequestService.deleteById(500);

    }

    @Test
    void testDeleteById_ServiceRequestNotFound() {
        when(serviceRequestRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                serviceRequestService.deleteById(999)
        );

        assertEquals("Service request not found with ID: 999", ex.getMessage());
        
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}