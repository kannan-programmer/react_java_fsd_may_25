package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import com.springboot.assetsphere.dto.AssetAuditDTO;
import com.springboot.assetsphere.enums.AuditStatus;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.*;
import com.springboot.assetsphere.repository.*;
import com.springboot.assetsphere.service.AssetAuditService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

@SpringBootTest
public class AssetAuditServiceTest {

    @Mock
    private AssetAuditRepository assetAuditRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private AssetRepository assetRepo;

    @Mock
    private AssetAuditDTO assetAuditDTO;

    @Mock
    private AssetAllocationRepository assetAllocateRepo;

    private AssetAuditService assetAuditService;

    private AssetAudit assetAudit;
    private Asset asset;
    private Employee employee;
    private User user;
    private AssetAllocation assetAllocation;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);

        assetAuditService = new AssetAuditService(
                assetAuditRepo,
                employeeRepo,
                assetRepo,
                assetAuditDTO,
                assetAllocateRepo
        );

        user = new User();
        user.setUsername("john.doe@example.com");
        user.setId(1);

        employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setJobTitle("Engineer");
        employee.setImageUrl("img.jpg");
        employee.setUser(user);

        asset = new Asset();
        asset.setId(1);
        asset.setAssetName("Monitor");
        asset.setModel("Dell P2419H");
        asset.setImageUrl("monitor.jpg");

        assetAllocation = new AssetAllocation();
        assetAllocation.setId(500);
        assetAllocation.setAsset(asset);
        assetAllocation.setEmployee(employee);
        assetAllocation.setAllocatedAt(LocalDateTime.now().toLocalDate());

        assetAudit = new AssetAudit();
        assetAudit.setId(100);
        assetAudit.setEmployee(employee);
        assetAudit.setAsset(asset);
        assetAudit.setAssetallocation(assetAllocation);
        assetAudit.setAuditedAt(LocalDateTime.now());
        assetAudit.setStatus(AuditStatus.PENDING);
        assetAudit.setComments("Check cables");
    }

    

    @Test
    public void testCreateAssetAudit_Success() throws ResourceNotFoundException, EmployeeNotFoundException, AssetNotFoundException {
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetAllocateRepo.findById(assetAllocation.getId())).thenReturn(Optional.of(assetAllocation));
        when(assetAuditRepo.save(any(AssetAudit.class))).thenReturn(assetAudit);

        AssetAudit result = assetAuditService.createAssetAudit(employee.getId(), assetAllocation.getId(), new AssetAudit());

        assertNotNull(result);
        assertEquals(assetAudit, result);
        assertEquals(employee.getId(), result.getEmployee().getId());
        assertEquals(assetAllocation.getId(), result.getAssetallocation().getId());
        assertEquals(asset.getId(), result.getAsset().getId());
        assertNotNull(result.getAuditedAt());
        assertEquals(AuditStatus.PENDING, result.getStatus());
    }

    @Test
    public void testCreateAssetAudit_EmployeeNotFound() {
        when(employeeRepo.findById(999)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class, () ->
                assetAuditService.createAssetAudit(999, assetAllocation.getId(), new AssetAudit()));

        assertEquals("Employee Not Found, Id Given is Invalid!", ex.getMessage());
    }

    @Test
    public void testCreateAssetAudit_AssetAllocationNotFound() {
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetAllocateRepo.findById(999)).thenReturn(Optional.empty());

        AssetNotFoundException ex = assertThrows(AssetNotFoundException.class, () ->
                assetAuditService.createAssetAudit(employee.getId(), 999, new AssetAudit()));

        assertEquals("Asset Allocation Not Found, Id Given is Invalid!", ex.getMessage());
    }

    @Test
    public void testCreateAssetAudit_AssetInAllocationIsNull() {
        AssetAllocation invalidAllocation = new AssetAllocation();
        invalidAllocation.setId(501);
        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetAllocateRepo.findById(invalidAllocation.getId())).thenReturn(Optional.of(invalidAllocation));

        AssetNotFoundException ex = assertThrows(AssetNotFoundException.class, () ->
                assetAuditService.createAssetAudit(employee.getId(), invalidAllocation.getId(), new AssetAudit()));

        assertEquals("Associated Asset is null in AssetAllocation!", ex.getMessage());
    }

    @Test
    public void testGetAllAudits() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAudit> audits = List.of(assetAudit);
        Page<AssetAudit> page = new PageImpl<>(audits);

        when(assetAuditRepo.findAll(pageable)).thenReturn(page);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(
            List.of(createExpectedAssetAuditDTO(assetAudit))
        );

        List<AssetAuditDTO> result = assetAuditService.getAllAudits(0, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(assetAudit.getEmployee().getUser().getUsername(), result.get(0).getUsername());
        assertEquals(assetAudit.getAsset().getAssetName(), result.get(0).getAssetName());
        assertEquals(assetAudit.getStatus().toString(), result.get(0).getStatus());
    }

    @Test
    public void testGetAllAudits_EmptyList() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAudit> emptyList = Collections.emptyList();
        Page<AssetAudit> emptyPage = new PageImpl<>(emptyList);

        when(assetAuditRepo.findAll(pageable)).thenReturn(emptyPage);
        when(assetAuditDTO.convertAssetAuditToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAuditDTO> result = assetAuditService.getAllAudits(0, 5);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAuditsByEmployee_Success() {
        List<AssetAudit> audits = List.of(assetAudit);
        when(assetAuditRepo.findByEmployeeId(employee.getId())).thenReturn(audits);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(
            List.of(createExpectedAssetAuditDTO(assetAudit))
        );

        List<AssetAuditDTO> result = assetAuditService.getAuditsByEmployee(employee.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employee.getName(), result.get(0).getUserFullName());
    }

    @Test
    public void testGetAuditsByEmployee_NoAuditsFound() {
        List<AssetAudit> emptyList = Collections.emptyList();
        when(assetAuditRepo.findByEmployeeId(999)).thenReturn(emptyList);
        when(assetAuditDTO.convertAssetAuditToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAuditDTO> result = assetAuditService.getAuditsByEmployee(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAuditsByAsset_Success() {
        List<AssetAudit> audits = List.of(assetAudit);
        when(assetAuditRepo.findByAssetId(asset.getId())).thenReturn(audits);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(
            List.of(createExpectedAssetAuditDTO(assetAudit))
        );

        List<AssetAuditDTO> result = assetAuditService.getAuditsByAsset(asset.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(asset.getAssetName(), result.get(0).getAssetName());
    }

    @Test
    public void testGetAuditsByAsset_NoAuditsFound() {
        List<AssetAudit> emptyList = Collections.emptyList();
        when(assetAuditRepo.findByAssetId(999)).thenReturn(emptyList);
        when(assetAuditDTO.convertAssetAuditToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAuditDTO> result = assetAuditService.getAuditsByAsset(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAuditsByStatus_ValidStatus_Success() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAudit> audits = List.of(assetAudit);
        Page<AssetAudit> page = new PageImpl<>(audits);

        when(assetAuditRepo.findByStatus(AuditStatus.PENDING, pageable)).thenReturn(page);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(
            List.of(createExpectedAssetAuditDTO(assetAudit))
        );

        List<AssetAuditDTO> result = assetAuditService.getAuditsByStatus("PENDING", 0, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }

    @Test
    public void testGetAuditsByStatus_InvalidStatus_ThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                assetAuditService.getAuditsByStatus("INVALID_STATUS", 0, 5));

        assertEquals("No enum constant com.springboot.assetsphere.enums.AuditStatus.INVALID_STATUS", ex.getMessage());
    }

    @Test
    public void testGetAuditsByStatus_NoAuditsFound() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAudit> emptyList = Collections.emptyList();
        Page<AssetAudit> emptyPage = new PageImpl<>(emptyList);

        when(assetAuditRepo.findByStatus(AuditStatus.PENDING, pageable)).thenReturn(emptyPage);
        when(assetAuditDTO.convertAssetAuditToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAuditDTO> result = assetAuditService.getAuditsByStatus("PENDING", 0, 5);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAuditById_Success() throws ResourceNotFoundException {
        when(assetAuditRepo.findById(assetAudit.getId())).thenReturn(Optional.of(assetAudit));

        AssetAudit result = assetAuditService.getAuditById(assetAudit.getId());

        assertNotNull(result);
        assertEquals(assetAudit, result);
    }

    @Test
    public void testGetAuditById_NotFound() {
        when(assetAuditRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetAuditService.getAuditById(999));

        assertEquals("Audit not found", ex.getMessage());
    }

    @Test
    public void testGetAuditsByUsername_Success() {
        List<AssetAudit> audits = List.of(assetAudit);
        when(assetAuditRepo.findByEmployeeUserUsername(user.getUsername())).thenReturn(audits);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(
            List.of(createExpectedAssetAuditDTO(assetAudit))
        );

        List<AssetAuditDTO> result = assetAuditService.getAuditsByUsername(user.getUsername());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testGetAuditsByUsername_NoAuditsFound() {
        List<AssetAudit> emptyList = Collections.emptyList();
        when(assetAuditRepo.findByEmployeeUserUsername("nonexistent@example.com")).thenReturn(emptyList);
        when(assetAuditDTO.convertAssetAuditToDto(emptyList)).thenReturn(Collections.emptyList());

        List<AssetAuditDTO> result = assetAuditService.getAuditsByUsername("nonexistent@example.com");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateAuditById_Success() throws ResourceNotFoundException {
        AssetAudit updatedAuditInput = new AssetAudit();
        updatedAuditInput.setStatus(AuditStatus.VERIFIED);
        updatedAuditInput.setComments("Updated comments for audit.");

        when(assetAuditRepo.findById(assetAudit.getId())).thenReturn(Optional.of(assetAudit));
        when(assetAuditRepo.save(any(AssetAudit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssetAudit result = assetAuditService.updateAuditById(assetAudit.getId(), updatedAuditInput);

        assertNotNull(result);
        assertEquals(assetAudit.getId(), result.getId());
        assertEquals(AuditStatus.VERIFIED, result.getStatus());
        assertEquals("Updated comments for audit.", result.getComments());
    }

    @Test
    public void testUpdateAuditById_NotFound() {
        AssetAudit updatedAuditInput = new AssetAudit();
        updatedAuditInput.setStatus(AuditStatus.VERIFIED);
        when(assetAuditRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetAuditService.updateAuditById(999, updatedAuditInput));

        assertEquals("Audit not found with ID: 999", ex.getMessage());
    }

    @Test
    public void testDeleteAuditById_Success() throws ResourceNotFoundException {
        when(assetAuditRepo.findById(assetAudit.getId())).thenReturn(Optional.of(assetAudit));
        doNothing().when(assetAuditRepo).delete(assetAudit);

        assetAuditService.deleteAuditById(assetAudit.getId());
    }

    @Test
    public void testDeleteAuditById_NotFound() {
        when(assetAuditRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetAuditService.deleteAuditById(999));

        assertEquals("Audit not found with ID: 999", ex.getMessage());
    }

    private AssetAuditDTO createExpectedAssetAuditDTO(AssetAudit audit) {
        AssetAuditDTO dto = new AssetAuditDTO();
        dto.setId(audit.getId());
        dto.setUsername(audit.getEmployee().getUser().getUsername());
        dto.setUserFullName(audit.getEmployee().getName());
        dto.setJobTitle(audit.getEmployee().getJobTitle());
        dto.setUserImageUrl(audit.getEmployee().getImageUrl());

        dto.setAssetId(audit.getAssetallocation().getId());
        dto.setAssetName(audit.getAsset().getAssetName());
        dto.setAssetModel(audit.getAsset().getModel());
        dto.setAssetImageUrl(audit.getAsset().getImageUrl());

        dto.setStatus(audit.getStatus() != null ? audit.getStatus().toString() : "PENDING");
        dto.setComments(audit.getComments());
        return dto;
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
        assetAuditRepo = null;
        employeeRepo = null;
        assetRepo = null;
        assetAuditDTO = null;
        assetAllocateRepo = null;
        assetAuditService = null;
        assetAudit = null;
        asset = null;
        employee = null;
        user = null;
        assetAllocation = null;
    }
}