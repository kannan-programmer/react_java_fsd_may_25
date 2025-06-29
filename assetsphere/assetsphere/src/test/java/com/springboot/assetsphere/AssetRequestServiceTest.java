package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.springboot.assetsphere.dto.AssetRequestDTO;
import com.springboot.assetsphere.enums.*;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.*;
import com.springboot.assetsphere.repository.*;
import com.springboot.assetsphere.service.AssetRequestService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

@SpringBootTest
public class AssetRequestServiceTest {

    @Mock
    private AssetRequestRepository assetRequestRepo;
    @Mock
    private EmployeeRepository employeeRepo;
    @Mock
    private AssetRepository assetRepo;
    @Mock
    private AssetAllocationRepository assetAllocationRepo;

    @Mock
    private AssetRequestDTO assetRequestDTO;

    private AssetRequestService assetRequestService;

    private Employee employee;
    private Asset asset;
    private AssetRequest assetRequest;
    private User user;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);

        assetRequestService = new AssetRequestService(
                assetRequestRepo,
                employeeRepo,
                assetRepo,
                assetAllocationRepo,
                assetRequestDTO
        );

        user = new User();
        user.setId(1);
        user.setUsername("john.doe@example.com");

        employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setJobTitle("Developer");
        employee.setImageUrl("john.png");
        employee.setUser(user);

        asset = new Asset();
        asset.setId(1);
        asset.setAssetName("Dell Laptop");
        asset.setModel("Latitude 7410");
        asset.setImageUrl("laptop.png");
        asset.setStatus(AssetStatus.AVAILABLE);

        assetRequest = new AssetRequest();
        assetRequest.setId(100);
        assetRequest.setEmployee(employee);
        assetRequest.setAsset(asset);
        assetRequest.setRequestedAt(LocalDateTime.now().minusDays(5));
        assetRequest.setStatus(RequestStatus.PENDING);
        assetRequest.setDescription("Need for project development");
    }

    
    @Test
    public void testAddAssetRequest_Success() throws ResourceNotFoundException, EmployeeNotFoundException, AssetNotFoundException {
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetRepo.findById(asset.getId())).thenReturn(Optional.of(asset));

        when(assetRequestRepo.save(any(AssetRequest.class))).thenAnswer(invocation -> {
            AssetRequest argumentRequest = invocation.getArgument(0);
            argumentRequest.setId(200);
            argumentRequest.setRequestedAt(LocalDateTime.now());
            argumentRequest.setStatus(RequestStatus.PENDING);
            return argumentRequest;
        });

        AssetRequest newRequestInput = new AssetRequest();
        newRequestInput.setDescription("New request description");
        newRequestInput.setStatus(null);
        newRequestInput.setRequestedAt(null);

        AssetRequest savedRequest = assetRequestService.addAssetRequest(employee.getId(), asset.getId(), newRequestInput);

        assertNotNull(savedRequest);
        assertEquals(newRequestInput.getDescription(), savedRequest.getDescription());
        assertEquals(employee.getId(), savedRequest.getEmployee().getId());
        assertEquals(asset.getId(), savedRequest.getAsset().getId());
        assertNotNull(savedRequest.getRequestedAt());
        assertEquals(RequestStatus.PENDING, savedRequest.getStatus());
        assertEquals(200, savedRequest.getId());
    }

    @Test
    public void testAddAssetRequest_EmployeeNotFound() {
        when(employeeRepo.findById(999)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, () ->
                assetRequestService.addAssetRequest(999, asset.getId(), new AssetRequest())
        );
        assertEquals("Employee not found", ex.getMessage());
    }

    @Test
    public void testAddAssetRequest_AssetNotFound() {
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetRepo.findById(999)).thenReturn(Optional.empty());

        AssetNotFoundException ex = assertThrows(AssetNotFoundException.class, () ->
                assetRequestService.addAssetRequest(employee.getId(), 999, new AssetRequest())
        );
        assertEquals("Asset not found", ex.getMessage());
    }

    @Test
    public void testGetAllAssetRequest_Success() {
        List<AssetRequest> mockList = List.of(assetRequest);
        Pageable pageable = PageRequest.of(0, 2);
        Page<AssetRequest> page = new PageImpl<>(mockList, pageable, mockList.size());

        when(assetRequestRepo.findAll(pageable)).thenReturn(page);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of(createExpectedAssetRequestDTO(assetRequest)));

        List<AssetRequestDTO> result = assetRequestService.getAllAssetRequest(0, 2);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
        assertEquals(asset.getAssetName(), result.get(0).getAssetName());
    }

    @Test
    public void testGetAllAssetRequest_EmptyList() {
        List<AssetRequest> emptyList = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 2);
        Page<AssetRequest> emptyPage = new PageImpl<>(emptyList, pageable, 0);

        when(assetRequestRepo.findAll(pageable)).thenReturn(emptyPage);
        when(assetRequestDTO.convertAssetRequestToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetRequestDTO> result = assetRequestService.getAllAssetRequest(0, 2);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByEmployeeId_Success() {
        List<AssetRequest> mockList = List.of(assetRequest);
        when(assetRequestRepo.findByEmployeeId(employee.getId())).thenReturn(mockList);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of(createExpectedAssetRequestDTO(assetRequest)));

        List<AssetRequestDTO> result = assetRequestService.getByEmployeeId(employee.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employee.getName(), result.get(0).getUserFullName());
    }

    @Test
    public void testGetByEmployeeId_NoRequestsFound() {
        List<AssetRequest> emptyList = Collections.emptyList();
        when(assetRequestRepo.findByEmployeeId(999)).thenReturn(emptyList);
        when(assetRequestDTO.convertAssetRequestToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetRequestDTO> result = assetRequestService.getByEmployeeId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByAssetId_Success() {
        List<AssetRequest> mockList = List.of(assetRequest);
        when(assetRequestRepo.findByAssetId(asset.getId())).thenReturn(mockList);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of(createExpectedAssetRequestDTO(assetRequest)));

        List<AssetRequestDTO> result = assetRequestService.getByAssetId(asset.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(asset.getAssetName(), result.get(0).getAssetName());
    }

    @Test
    public void testGetByAssetId_NoRequestsFound() {
        List<AssetRequest> emptyList = Collections.emptyList();
        when(assetRequestRepo.findByAssetId(999)).thenReturn(emptyList);
        when(assetRequestDTO.convertAssetRequestToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetRequestDTO> result = assetRequestService.getByAssetId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByStatus_ValidStatus_Success() {
        List<AssetRequest> mockList = List.of(assetRequest);
        when(assetRequestRepo.findByStatus(RequestStatus.PENDING)).thenReturn(mockList);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of(createExpectedAssetRequestDTO(assetRequest)));

        List<AssetRequestDTO> result = assetRequestService.getByStatus("PENDING");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }

    @Test
    public void testGetByStatus_InvalidStatus_ThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                assetRequestService.getByStatus("INVALID_STATUS"));

        assertEquals("No enum constant com.springboot.assetsphere.enums.RequestStatus.INVALID_STATUS", ex.getMessage());
    }

    @Test
    public void testGetByStatus_NoRequestsFound() {
        List<AssetRequest> emptyList = Collections.emptyList();
        when(assetRequestRepo.findByStatus(RequestStatus.REJECTED)).thenReturn(emptyList);
        when(assetRequestDTO.convertAssetRequestToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetRequestDTO> result = assetRequestService.getByStatus("REJECTED");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetByUsername_Success() {
        List<AssetRequest> mockList = List.of(assetRequest);
        when(assetRequestRepo.findByEmployeeUserUsername(user.getUsername())).thenReturn(mockList);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of(createExpectedAssetRequestDTO(assetRequest)));

        List<AssetRequestDTO> result = assetRequestService.getByUsername(user.getUsername());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testGetByUsername_NoRequestsFound() {
        List<AssetRequest> emptyList = Collections.emptyList();
        when(assetRequestRepo.findByEmployeeUserUsername("nonexistent@example.com")).thenReturn(emptyList);
        when(assetRequestDTO.convertAssetRequestToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetRequestDTO> result = assetRequestService.getByUsername("nonexistent@example.com");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateById_ApprovedStatus() throws ResourceNotFoundException {
        AssetRequest updatedRequestInput = new AssetRequest();
        updatedRequestInput.setStatus(RequestStatus.APPROVED);
        updatedRequestInput.setAdminComments("Request approved. Asset allocated.");

        when(assetRequestRepo.findById(assetRequest.getId())).thenReturn(Optional.of(assetRequest));
        when(assetRequestRepo.save(any(AssetRequest.class))).thenAnswer(invocation -> {
            AssetRequest saved = invocation.getArgument(0);
            saved.setResolvedAt(LocalDateTime.now());
            return saved;
        });
        when(assetAllocationRepo.save(any(AssetAllocation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(assetRepo.save(any(Asset.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssetRequest result = assetRequestService.updateById(assetRequest.getId(), updatedRequestInput);

        assertNotNull(result);
        assertEquals(assetRequest.getId(), result.getId());
        assertEquals(RequestStatus.APPROVED, result.getStatus());
        assertEquals("Request approved. Asset allocated.", result.getAdminComments());
        assertNotNull(result.getResolvedAt());
        assertEquals(AssetStatus.ASSIGNED, asset.getStatus());
    }

    @Test
    public void testUpdateById_RejectedStatus() throws ResourceNotFoundException {
        AssetRequest updatedRequestInput = new AssetRequest();
        updatedRequestInput.setStatus(RequestStatus.REJECTED);
        updatedRequestInput.setAdminComments("Request rejected due to unavailability.");

        when(assetRequestRepo.findById(assetRequest.getId())).thenReturn(Optional.of(assetRequest));
        when(assetRequestRepo.save(any(AssetRequest.class))).thenAnswer(invocation -> {
            AssetRequest saved = invocation.getArgument(0);
            saved.setResolvedAt(LocalDateTime.now());
            return saved;
        });

        AssetRequest result = assetRequestService.updateById(assetRequest.getId(), updatedRequestInput);

        assertNotNull(result);
        assertEquals(assetRequest.getId(), result.getId());
        assertEquals(RequestStatus.REJECTED, result.getStatus());
        assertEquals("Request rejected due to unavailability.", result.getAdminComments());
        assertNotNull(result.getResolvedAt());
        assertEquals(AssetStatus.AVAILABLE, asset.getStatus());
    }

    @Test
    public void testUpdateById_NotFound() {
        AssetRequest updatedRequestInput = new AssetRequest();
        updatedRequestInput.setStatus(RequestStatus.APPROVED);
        when(assetRequestRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetRequestService.updateById(999, updatedRequestInput));

        assertEquals("Asset request not found with ID: 999", ex.getMessage());
    }

    @Test
    public void testDeleteById_Success() throws ResourceNotFoundException {
        when(assetRequestRepo.findById(assetRequest.getId())).thenReturn(Optional.of(assetRequest));
        doNothing().when(assetRequestRepo).delete(assetRequest);

        assetRequestService.deleteById(assetRequest.getId());
    }

    @Test
    public void testDeleteById_NotFound() {
        when(assetRequestRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetRequestService.deleteById(999));

        assertEquals("Asset request not found with ID: 999", ex.getMessage());
    }

    private AssetRequestDTO createExpectedAssetRequestDTO(AssetRequest request) {
        AssetRequestDTO dto = new AssetRequestDTO();
        dto.setId(request.getId());
        dto.setUsername(request.getEmployee().getUser().getUsername());
        dto.setUserFullName(request.getEmployee().getName());
        dto.setJobTitle(request.getEmployee().getJobTitle());
        dto.setUserImageUrl(request.getEmployee().getImageUrl());

        dto.setAssetId(request.getAsset().getId());
        dto.setAssetName(request.getAsset().getAssetName());
        dto.setAssetModel(request.getAsset().getModel());
        dto.setAssetImageUrl(request.getAsset().getImageUrl());

        dto.setRequestedAt(request.getRequestedAt());
        dto.setResolvedAt(request.getResolvedAt());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus().toString());
        dto.setAdminComments(request.getAdminComments());
        return dto;
    }
    
    @AfterEach
    public void cleanup() throws Exception {
        closeable.close();
        assetRequestRepo = null;
        employeeRepo = null;
        assetRepo = null;
        assetAllocationRepo = null;
        assetRequestDTO = null;
        assetRequestService = null;
        employee = null;
        asset = null;
        assetRequest = null;
        user = null;
    }

}