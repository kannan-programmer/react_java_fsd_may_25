package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.springboot.assetsphere.dto.AssetAllocationDTO;
import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetAuditRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetReturnRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.service.AssetAllocationService;

@SpringBootTest
public class AssetAllocationServiceTest {

    private AssetAllocationService assetAllocationService;

    @Mock
    private AssetAllocationRepository assetAllocationRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private AssetRepository assetRepo;

    @Mock 
    private AssetAllocationDTO assetAllocationDTO;

    @Mock
    private AssetReturnRequestRepository assetReturnRequestRepo;

    @Mock
    private AssetAuditRepository assetauditRepo;

    private AutoCloseable closeable; 
    private Employee employee;
    private Asset asset;
    private AssetAllocation allocation;
    private User user;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this); 

        this.assetAllocationService = new AssetAllocationService(
                assetAllocationRepo,
                employeeRepo,
                assetRepo,
                assetAllocationDTO,
                assetReturnRequestRepo,
                assetauditRepo
        );

        user = new User();
        user.setId(100);
        user.setUsername("john.doe@example.com");

        employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setJobTitle("Software Engineer");
        employee.setImageUrl("john.jpg");
        employee.setUser(user);

        asset = new Asset();
        asset.setId(1);
        asset.setAssetName("Laptop");
        asset.setModel("Dell XPS 15");
        asset.setImageUrl("laptop.jpg");
        asset.setStatus(AssetStatus.AVAILABLE);

        allocation = new AssetAllocation();
        allocation.setId(1);
        allocation.setAllocatedAt(LocalDate.now());
        allocation.setStatus(AllocationStatus.ASSIGNED);
        allocation.setEmployee(employee);
        allocation.setAsset(asset);
    }

   
    @Test
    public void testAddAllocation_Success() throws EmployeeNotFoundException, AssetNotFoundException, IllegalStateException {
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetRepo.findById(asset.getId())).thenReturn(Optional.of(asset));
        when(assetAllocationRepo.save(any(AssetAllocation.class))).thenReturn(allocation);
        when(assetRepo.save(any(Asset.class))).thenReturn(asset);

        AssetAllocation saved = assetAllocationService.addAllocation(employee.getId(), asset.getId(), new AssetAllocation());

        assertNotNull(saved);
        assertEquals(employee.getId(), saved.getEmployee().getId());
        assertEquals(asset.getId(), saved.getAsset().getId());
        assertEquals(LocalDate.now(), saved.getAllocatedAt()); 
        assertEquals(AllocationStatus.ASSIGNED, saved.getStatus()); 
    }

    @Test
    public void testAddAllocation_EmployeeNotFound() {
        when(employeeRepo.findById(999)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,
                () -> assetAllocationService.addAllocation(999, asset.getId(), new AssetAllocation()));

        assertEquals("Employee not found with ID: 999", ex.getMessage());
    }

    @Test
    public void testAddAllocation_AssetNotFound() {
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetRepo.findById(999)).thenReturn(Optional.empty());

        AssetNotFoundException ex = assertThrows(AssetNotFoundException.class,
                () -> assetAllocationService.addAllocation(employee.getId(), 999, new AssetAllocation()));

        assertEquals("Asset not found with ID: 999", ex.getMessage());
    }

    @Test
    public void testAddAllocation_AssetAlreadyAssigned() {
        asset.setStatus(AssetStatus.ASSIGNED);
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetRepo.findById(asset.getId())).thenReturn(Optional.of(asset));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> assetAllocationService.addAllocation(employee.getId(), asset.getId(), new AssetAllocation()));

        assertEquals("Asset with ID " + asset.getId() + " is already allocated or in use.", ex.getMessage());
    }

    @Test
    public void testAddAllocation_AssetInService() {
        asset.setStatus(AssetStatus.IN_SERVICE);
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetRepo.findById(asset.getId())).thenReturn(Optional.of(asset));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> assetAllocationService.addAllocation(employee.getId(), asset.getId(), new AssetAllocation()));

        assertEquals("Asset with ID " + asset.getId() + " is already allocated or in use.", ex.getMessage());
    }
    
    @Test
    public void testGetAllAllocations() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetAllocation> allocations = List.of(allocation); 
        when(assetAllocationRepo.findAll(pageable)).thenReturn(new PageImpl<>(allocations));

        // Mock the DTO conversion explicitly since assetAllocationDTO is a Mock
        List<AssetAllocationDTO> expectedDtoList = new ArrayList<>();
        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setId(allocation.getId());
        dto.setEmployeeId(employee.getId());
        dto.setUsername(user.getUsername());
        dto.setUserFullName(employee.getName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setUserImageUrl(employee.getImageUrl());
        dto.setAssetId(asset.getId());
        dto.setAssetName(asset.getAssetName());
        dto.setAssetModel(asset.getModel());
        dto.setAssetImageUrl(asset.getImageUrl());
        dto.setAllocatedAt(allocation.getAllocatedAt());
        dto.setReturnedAt(allocation.getReturnedAt());
        dto.setStatus(allocation.getStatus().toString());
        expectedDtoList.add(dto);
        when(assetAllocationDTO.convertAssetAllocationToDto(allocations)).thenReturn(expectedDtoList);

        List<AssetAllocationDTO> result = assetAllocationService.getAllAllocations(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDtoList.get(0).getUsername(), result.get(0).getUsername());
        assertEquals(expectedDtoList.get(0).getAssetName(), result.get(0).getAssetName());
        assertEquals(expectedDtoList.get(0).getStatus(), result.get(0).getStatus());
    }

    @Test
    public void testGetAllAllocations_EmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetAllocation> emptyList = Collections.emptyList();
        when(assetAllocationRepo.findAll(pageable)).thenReturn(new PageImpl<>(emptyList));
        when(assetAllocationDTO.convertAssetAllocationToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAllocationDTO> result = assetAllocationService.getAllAllocations(0, 10);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllocationsByEmployee_Success() {
        List<AssetAllocation> allocations = List.of(allocation);
        when(assetAllocationRepo.findByEmployeeId(employee.getId())).thenReturn(allocations);

        // Mock the DTO conversion explicitly
        List<AssetAllocationDTO> expectedDtoList = new ArrayList<>();
        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setId(allocation.getId());
        dto.setEmployeeId(employee.getId());
        dto.setUsername(user.getUsername());
        dto.setUserFullName(employee.getName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setUserImageUrl(employee.getImageUrl());
        dto.setAssetId(asset.getId());
        dto.setAssetName(asset.getAssetName());
        dto.setAssetModel(asset.getModel());
        dto.setAssetImageUrl(asset.getImageUrl());
        dto.setAllocatedAt(allocation.getAllocatedAt());
        dto.setReturnedAt(allocation.getReturnedAt());
        dto.setStatus(allocation.getStatus().toString());
        expectedDtoList.add(dto);
        when(assetAllocationDTO.convertAssetAllocationToDto(allocations)).thenReturn(expectedDtoList);

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByEmployee(employee.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employee.getName(), result.get(0).getUserFullName());
        assertEquals(asset.getAssetName(), result.get(0).getAssetName());
    }

    @Test
    public void testGetAllocationsByEmployee_NoAllocations() {
        List<AssetAllocation> emptyList = Collections.emptyList();
        when(assetAllocationRepo.findByEmployeeId(999)).thenReturn(emptyList);
        when(assetAllocationDTO.convertAssetAllocationToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByEmployee(999);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllocationsByAsset_Success() {
        List<AssetAllocation> allocations = List.of(allocation);
        when(assetAllocationRepo.findByAssetId(asset.getId())).thenReturn(allocations);

        // Mock the DTO conversion explicitly
        List<AssetAllocationDTO> expectedDtoList = new ArrayList<>();
        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setId(allocation.getId());
        dto.setEmployeeId(employee.getId());
        dto.setUsername(user.getUsername());
        dto.setUserFullName(employee.getName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setUserImageUrl(employee.getImageUrl());
        dto.setAssetId(asset.getId());
        dto.setAssetName(asset.getAssetName());
        dto.setAssetModel(asset.getModel());
        dto.setAssetImageUrl(asset.getImageUrl());
        dto.setAllocatedAt(allocation.getAllocatedAt());
        dto.setReturnedAt(allocation.getReturnedAt());
        dto.setStatus(allocation.getStatus().toString());
        expectedDtoList.add(dto);
        when(assetAllocationDTO.convertAssetAllocationToDto(allocations)).thenReturn(expectedDtoList);

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByAsset(asset.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(asset.getAssetName(), result.get(0).getAssetName());
    }

    @Test
    public void testGetAllocationsByAsset_NoAllocations() {
        List<AssetAllocation> emptyList = Collections.emptyList();
        when(assetAllocationRepo.findByAssetId(999)).thenReturn(emptyList);
        when(assetAllocationDTO.convertAssetAllocationToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByAsset(999);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllocationsByStatus_ValidStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetAllocation> allocations = List.of(allocation);
        when(assetAllocationRepo.findByStatus(AllocationStatus.ASSIGNED, pageable)).thenReturn(new PageImpl<>(allocations));

        // Mock the DTO conversion explicitly
        List<AssetAllocationDTO> expectedDtoList = new ArrayList<>();
        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setId(allocation.getId());
        dto.setEmployeeId(employee.getId());
        dto.setUsername(user.getUsername());
        dto.setUserFullName(employee.getName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setUserImageUrl(employee.getImageUrl());
        dto.setAssetId(asset.getId());
        dto.setAssetName(asset.getAssetName());
        dto.setAssetModel(asset.getModel());
        dto.setAssetImageUrl(asset.getImageUrl());
        dto.setAllocatedAt(allocation.getAllocatedAt());
        dto.setReturnedAt(allocation.getReturnedAt());
        dto.setStatus(allocation.getStatus().toString());
        expectedDtoList.add(dto);
        when(assetAllocationDTO.convertAssetAllocationToDto(allocations)).thenReturn(expectedDtoList);

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByStatus("ASSIGNED", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ASSIGNED", result.get(0).getStatus());
    }

    @Test
    public void testGetAllocationsByStatus_InvalidStatus() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> assetAllocationService.getAllocationsByStatus("INVALID_STATUS", 0, 10));

        assertEquals("No enum constant com.springboot.assetsphere.enums.AllocationStatus.INVALID_STATUS", ex.getMessage());
    }

    @Test
    public void testGetAllocationsByUsername_Success() {
        List<AssetAllocation> allocations = List.of(allocation);
        when(assetAllocationRepo.findByEmployeeUserUsername(user.getUsername())).thenReturn(allocations);

        // Mock the DTO conversion explicitly
        List<AssetAllocationDTO> expectedDtoList = new ArrayList<>();
        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setId(allocation.getId());
        dto.setEmployeeId(employee.getId());
        dto.setUsername(user.getUsername());
        dto.setUserFullName(employee.getName());
        dto.setJobTitle(employee.getJobTitle());
        dto.setUserImageUrl(employee.getImageUrl());
        dto.setAssetId(asset.getId());
        dto.setAssetName(asset.getAssetName());
        dto.setAssetModel(asset.getModel());
        dto.setAssetImageUrl(asset.getImageUrl());
        dto.setAllocatedAt(allocation.getAllocatedAt());
        dto.setReturnedAt(allocation.getReturnedAt());
        dto.setStatus(allocation.getStatus().toString());
        expectedDtoList.add(dto);
        when(assetAllocationDTO.convertAssetAllocationToDto(allocations)).thenReturn(expectedDtoList);

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByUsername(user.getUsername());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testGetAllocationsByUsername_NoAllocations() {
        List<AssetAllocation> emptyList = Collections.emptyList();
        when(assetAllocationRepo.findByEmployeeUserUsername("nonexistent@example.com")).thenReturn(emptyList);
        when(assetAllocationDTO.convertAssetAllocationToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByUsername("nonexistent@example.com");

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testUpdateAllocation_Success() throws ResourceNotFoundException {
        AssetAllocation updatedAllocationInput = new AssetAllocation();
        updatedAllocationInput.setAllocatedAt(LocalDate.of(2023, 1, 1));
        updatedAllocationInput.setReturnedAt(LocalDate.of(2024, 1, 1));
        updatedAllocationInput.setStatus(AllocationStatus.RETURNED);

        AssetAllocation originalDbAllocation = new AssetAllocation();
        originalDbAllocation.setId(1);
        originalDbAllocation.setAllocatedAt(LocalDate.now());
        originalDbAllocation.setStatus(AllocationStatus.ASSIGNED);
        originalDbAllocation.setEmployee(employee);
        originalDbAllocation.setAsset(asset);

        when(assetAllocationRepo.findById(1)).thenReturn(Optional.of(originalDbAllocation));
        when(assetAllocationRepo.save(any(AssetAllocation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssetAllocation result = assetAllocationService.updateAllocation(1, updatedAllocationInput);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(LocalDate.of(2023, 1, 1), result.getAllocatedAt());
        assertEquals(LocalDate.of(2024, 1, 1), result.getReturnedAt());
        assertEquals(AllocationStatus.RETURNED, result.getStatus());
    }

    @Test
    public void testUpdateAllocation_NotFound() {
        when(assetAllocationRepo.findById(999)).thenReturn(Optional.empty());
        AssetAllocation updatedAllocationInput = new AssetAllocation();
        updatedAllocationInput.setAllocatedAt(LocalDate.of(2023, 1, 1));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> assetAllocationService.updateAllocation(999, updatedAllocationInput));

        assertEquals("Invalid Allocation ID: 999", ex.getMessage());
    }

    @Test
    public void testDeleteAllocation_Success() throws ResourceNotFoundException {
        when(assetAllocationRepo.findById(1)).thenReturn(Optional.of(allocation));
        
        doNothing().when(assetauditRepo).deleteByAssetAllocationId(1);
        doNothing().when(assetReturnRequestRepo).deleteByAllocationId(1);
        doNothing().when(assetAllocationRepo).delete(allocation);

        assetAllocationService.deleteAllocation(1);
    }

    @Test
    public void testDeleteAllocation_NotFound() {
        when(assetAllocationRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> assetAllocationService.deleteAllocation(999));

        assertEquals("Asset Allocation not found with ID: 999", ex.getMessage());
    }
    
    @AfterEach
    public void cleanup() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
        assetAllocationRepo = null;
        employeeRepo = null;
        assetRepo = null;
        assetAllocationDTO = null;
        assetReturnRequestRepo = null;
        assetauditRepo = null;
        assetAllocationService = null;
        employee = null;
        asset = null;
        allocation = null;
        user = null;
    }

}