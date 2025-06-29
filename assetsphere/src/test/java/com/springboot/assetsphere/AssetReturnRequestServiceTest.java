package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.AssetReturnRequestDTO;
import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.*;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetReturnRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.service.AssetReturnRequestService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssetReturnRequestServiceTest {

    @Mock
    private AssetReturnRequestRepository assetReturnRequestRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private AssetAllocationRepository assetAllocationRepo;

    @Mock
    private AssetReturnRequestDTO assetReturnRequestDTO;

    private AssetReturnRequestService assetReturnRequestService;

    private Employee employee;
    private AssetAllocation allocation;
    private AssetReturnRequest returnRequest;
    private List<AssetReturnRequest> mockList;
    private User user;
    private Asset asset;
    private AssetCategory assetCategory;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        assetReturnRequestService = new AssetReturnRequestService(
                assetReturnRequestRepo,
                employeeRepo,
                assetAllocationRepo,
                assetReturnRequestDTO
        );

        user = new User();
        user.setId(1);
        user.setUsername("john.doe@example.com");

        employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setJobTitle("Software Engineer");
        employee.setImageUrl("john.png");
        employee.setUser(user);

        assetCategory = new AssetCategory();
        assetCategory.setId(1);
        assetCategory.setName("Electronics");

        asset = new Asset();
        asset.setId(101);
        asset.setAssetName("Laptop");
        asset.setModel("Dell XPS 15");
        asset.setImageUrl("laptop.png");
        asset.setStatus(AssetStatus.ASSIGNED);

        allocation = new AssetAllocation();
        allocation.setId(10);
        allocation.setAsset(asset);
        allocation.setEmployee(employee);
        allocation.setAllocatedAt(LocalDate.now().minusMonths(6));
        allocation.setStatus(AllocationStatus.ASSIGNED);

        returnRequest = new AssetReturnRequest();
        returnRequest.setId(100);
        returnRequest.setEmployee(employee);
        returnRequest.setAllocation(allocation);
        returnRequest.setRequestedAt(LocalDateTime.now().minusDays(2));
        returnRequest.setStatus(RequestStatus.PENDING);
        returnRequest.setReason("Finished project, no longer needed.");
        returnRequest.setAdminComments(null);

        mockList = List.of(returnRequest);
    }

    

    @Test
    public void testAddAssetReturnRequest_Success() throws ResourceNotFoundException, EmployeeNotFoundException {
        AssetReturnRequest newReturnRequestInput = new AssetReturnRequest();
        newReturnRequestInput.setReason("Reason for return.");
        newReturnRequestInput.setStatus(null);
        newReturnRequestInput.setRequestedAt(null);

        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetAllocationRepo.findById(allocation.getId())).thenReturn(Optional.of(allocation));

        when(assetReturnRequestRepo.save(any(AssetReturnRequest.class))).thenAnswer(invocation -> {
            AssetReturnRequest argRequest = invocation.getArgument(0);
            argRequest.setId(101);
            argRequest.setRequestedAt(LocalDateTime.now());
            argRequest.setStatus(RequestStatus.PENDING);
            return argRequest;
        });

        AssetReturnRequest saved = assetReturnRequestService.addAssetReturnRequest(employee.getId(), allocation.getId(), newReturnRequestInput);

        assertNotNull(saved);
        assertEquals(newReturnRequestInput.getReason(), saved.getReason());
        assertEquals(employee.getId(), saved.getEmployee().getId());
        assertEquals(allocation.getId(), saved.getAllocation().getId());
        assertNotNull(saved.getRequestedAt());
        assertEquals(RequestStatus.PENDING, saved.getStatus());
        assertEquals(101, saved.getId());
    }

    @Test
    public void testAddAssetReturnRequest_AllocationNotFound() {
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetAllocationRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                assetReturnRequestService.addAssetReturnRequest(employee.getId(), 999, new AssetReturnRequest())
        );

        assertEquals("Allocation Not Found, ID is invalid", exception.getMessage());
    }

    @Test
    public void testGetAllAssetReturnRequests_Success() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<AssetReturnRequest> page = new PageImpl<>(mockList, pageable, mockList.size());

        when(assetReturnRequestRepo.findAll(pageable)).thenReturn(page);
        when(assetReturnRequestDTO.convertAssetReturnToDto(mockList)).thenReturn(List.of(createExpectedAssetReturnRequestDTO(returnRequest)));

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getAllAssetReturnRequests(0, 3);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(returnRequest.getId(), result.get(0).getId());
        assertEquals(user.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testGetAllAssetReturnRequests_EmptyList() {
        List<AssetReturnRequest> emptyList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 3);
        Page<AssetReturnRequest> emptyPage = new PageImpl<>(emptyList, pageable, 0);

        when(assetReturnRequestRepo.findAll(pageable)).thenReturn(emptyPage);
        when(assetReturnRequestDTO.convertAssetReturnToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getAllAssetReturnRequests(0, 3);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByEmployeeId_Success() {
        when(assetReturnRequestRepo.findByEmployeeId(employee.getId())).thenReturn(mockList);
        when(assetReturnRequestDTO.convertAssetReturnToDto(mockList)).thenReturn(List.of(createExpectedAssetReturnRequestDTO(returnRequest)));

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getByEmployeeId(employee.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employee.getName(), result.get(0).getUserFullName());
    }

    @Test
    public void testGetByEmployeeId_NoRequestsFound() {
        List<AssetReturnRequest> emptyList = Collections.emptyList();
        when(assetReturnRequestRepo.findByEmployeeId(999)).thenReturn(emptyList);
        when(assetReturnRequestDTO.convertAssetReturnToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getByEmployeeId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByStatus_ValidStatus_Success() {
        when(assetReturnRequestRepo.findByStatus(RequestStatus.PENDING)).thenReturn(mockList);
        when(assetReturnRequestDTO.convertAssetReturnToDto(mockList)).thenReturn(List.of(createExpectedAssetReturnRequestDTO(returnRequest)));

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getByStatus("PENDING");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }

    @Test
    public void testGetByStatus_InvalidStatus_ThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                assetReturnRequestService.getByStatus("INVALID_STATUS"));

        assertEquals("No enum constant com.springboot.assetsphere.enums.RequestStatus.INVALID_STATUS", ex.getMessage());
    }

    @Test
    public void testGetByStatus_NoRequestsFound() {
        List<AssetReturnRequest> emptyList = Collections.emptyList();
        when(assetReturnRequestRepo.findByStatus(RequestStatus.REJECTED)).thenReturn(emptyList);
        when(assetReturnRequestDTO.convertAssetReturnToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getByStatus("REJECTED");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByUsername_Success() {
        when(assetReturnRequestRepo.findByEmployeeUsername(user.getUsername())).thenReturn(mockList);
        when(assetReturnRequestDTO.convertAssetReturnToDto(mockList)).thenReturn(List.of(createExpectedAssetReturnRequestDTO(returnRequest)));

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getByUsername(user.getUsername());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testGetByUsername_NoRequestsFound() {
        List<AssetReturnRequest> emptyList = Collections.emptyList();
        when(assetReturnRequestRepo.findByEmployeeUsername("nonexistent.user")).thenReturn(emptyList);
        when(assetReturnRequestDTO.convertAssetReturnToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getByUsername("nonexistent.user");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateById_RejectedStatus() throws ResourceNotFoundException {
        AssetReturnRequest updatedRequestInput = new AssetReturnRequest();
        updatedRequestInput.setStatus(RequestStatus.REJECTED);
        updatedRequestInput.setAdminComments("Return rejected, asset still needed.");

        AssetReturnRequest existingReturnRequest = new AssetReturnRequest();
        existingReturnRequest.setId(returnRequest.getId());
        existingReturnRequest.setEmployee(returnRequest.getEmployee());
        existingReturnRequest.setAllocation(returnRequest.getAllocation());
        existingReturnRequest.setRequestedAt(returnRequest.getRequestedAt());
        existingReturnRequest.setStatus(RequestStatus.PENDING);
        existingReturnRequest.setReason(returnRequest.getReason());

        when(assetReturnRequestRepo.findById(existingReturnRequest.getId())).thenReturn(Optional.of(existingReturnRequest));
        when(assetReturnRequestRepo.save(any(AssetReturnRequest.class))).thenAnswer(invocation -> {
            AssetReturnRequest saved = invocation.getArgument(0);
            saved.setResolvedAt(LocalDateTime.now());
            return saved;
        });

        AssetReturnRequest result = assetReturnRequestService.updateById(existingReturnRequest.getId(), updatedRequestInput);

        assertNotNull(result);
        assertEquals(RequestStatus.REJECTED, result.getStatus());
        assertEquals("Return rejected, asset still needed.", result.getAdminComments());
        assertNotNull(result.getResolvedAt());
        assertNotNull(result.getAllocation());
        assertEquals(AssetStatus.ASSIGNED, result.getAllocation().getAsset().getStatus());
    }

    @Test
    public void testUpdateById_ReturnRequestNotFound() {
        AssetReturnRequest updatedRequest = new AssetReturnRequest();
        updatedRequest.setStatus(RequestStatus.APPROVED);
        when(assetReturnRequestRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetReturnRequestService.updateById(999, updatedRequest));

        assertEquals("Return request not found for ID: 999", ex.getMessage());
    }

    @Test
    public void testUpdateById_Approved_AllocationNotFoundForExistingRequest() {
        AssetReturnRequest updatedRequestInput = new AssetReturnRequest();
        updatedRequestInput.setStatus(RequestStatus.APPROVED);

        AssetReturnRequest existingReturnRequestWithoutAllocation = new AssetReturnRequest();
        existingReturnRequestWithoutAllocation.setId(returnRequest.getId());
        existingReturnRequestWithoutAllocation.setEmployee(employee);
        existingReturnRequestWithoutAllocation.setAllocation(null);

        when(assetReturnRequestRepo.findById(existingReturnRequestWithoutAllocation.getId())).thenReturn(Optional.of(existingReturnRequestWithoutAllocation));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetReturnRequestService.updateById(existingReturnRequestWithoutAllocation.getId(), updatedRequestInput));

        assertEquals("No allocation linked to this return request.", ex.getMessage());
    }

    @Test
    public void testDeleteById_Success() throws ResourceNotFoundException {
        when(assetReturnRequestRepo.findById(returnRequest.getId())).thenReturn(Optional.of(returnRequest));
        doNothing().when(assetReturnRequestRepo).delete(returnRequest);

        assetReturnRequestService.deleteById(returnRequest.getId());
    }

    @Test
    public void testDeleteById_ReturnRequestNotFound() {
        when(assetReturnRequestRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetReturnRequestService.deleteById(999));

        assertEquals("Return request not found for ID: 999", ex.getMessage());
    }

    private AssetReturnRequestDTO createExpectedAssetReturnRequestDTO(AssetReturnRequest request) {
        AssetReturnRequestDTO dto = new AssetReturnRequestDTO();
        dto.setId(request.getId());
        dto.setUsername(request.getEmployee().getUser().getUsername());
        dto.setUserFullName(request.getEmployee().getName());
        dto.setJobTitle(request.getEmployee().getJobTitle());
        dto.setUserImageUrl(request.getEmployee().getImageUrl());

        if (request.getAllocation() != null && request.getAllocation().getAsset() != null) {
            dto.setAllocationId(request.getAllocation().getId());
            dto.setAssetName(request.getAllocation().getAsset().getAssetName());
            dto.setAssetModel(request.getAllocation().getAsset().getModel());
            dto.setAssetImageUrl(request.getAllocation().getAsset().getImageUrl());
            if (request.getAllocation().getAsset().getCategory() != null) {
                dto.setAssetCategory(request.getAllocation().getAsset().getCategory().getName());
            } else {
                dto.setAssetCategory("N/A");
            }
        } else {
            dto.setAllocationId(0);
            dto.setAssetName("N/A");
            dto.setAssetModel("N/A");
            dto.setAssetImageUrl("N/A");
            dto.setAssetCategory("N/A");
        }

        dto.setReason(request.getReason());
        dto.setStatus(request.getStatus().toString());
        dto.setAdminComments(request.getAdminComments());
        dto.setRequestedAt(request.getRequestedAt());
        dto.setResolvedAt(request.getResolvedAt());
        return dto;
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
        assetReturnRequestRepo = null;
        employeeRepo = null;
        assetAllocationRepo = null;
        assetReturnRequestDTO = null;
        assetReturnRequestService = null;
        employee = null;
        allocation = null;
        returnRequest = null;
        mockList = null;
        user = null;
        asset = null;
        assetCategory = null;
    }
}