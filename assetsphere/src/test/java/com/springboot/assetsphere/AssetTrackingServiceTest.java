package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.AssetTrackingDTO;
import com.springboot.assetsphere.enums.TrackingAction;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.*;
import com.springboot.assetsphere.repository.*;
import com.springboot.assetsphere.service.AssetTrackingService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssetTrackingServiceTest {

    @Mock
    private AssetTrackingRepository assetTrackingRepo;
    @Mock
    private EmployeeRepository employeeRepo;
    @Mock
    private AssetRepository assetRepo;
    @Mock
    private AssetTrackingDTO assetTrackingDTO;

    @InjectMocks
    private AssetTrackingService assetTrackingService;

    private AutoCloseable closeable;

    private Employee employee;
    private Asset asset;
    private AssetTracking tracking;
    private User user;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("john@example.com");

        employee = new Employee();
        employee.setId(1);
        employee.setUser(user);
        employee.setName("John Doe");
        employee.setImageUrl("john.png");
        employee.setJobTitle("Engineer");

        asset = new Asset();
        asset.setId(10);
        asset.setAssetName("Laptop");
        asset.setModel("HP Pavilion");
        asset.setImageUrl("laptop.jpg");

        tracking = new AssetTracking();
        tracking.setId(100);
        tracking.setAction(TrackingAction.ALLOCATED);
        tracking.setEmployee(employee);
        tracking.setAsset(asset);
        tracking.setTimestamp(LocalDateTime.of(2023, 1, 15, 10, 0));
        tracking.setRemarks("Initial allocation to John");
    }

    @Test
    void testAddTrackingLog_Success() throws ResourceNotFoundException, AssetNotFoundException, EmployeeNotFoundException {
        AssetTracking newTrackingInput = new AssetTracking();
        newTrackingInput.setAction(TrackingAction.ALLOCATED);
        newTrackingInput.setRemarks("New allocation");

        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(assetRepo.findById(asset.getId())).thenReturn(Optional.of(asset));
        when(assetTrackingRepo.save(any(AssetTracking.class))).thenAnswer(invocation -> {
            AssetTracking savedTracking = invocation.getArgument(0);
            savedTracking.setId(200);
            savedTracking.setTimestamp(LocalDateTime.now());
            return savedTracking;
        });

        AssetTracking saved = assetTrackingService.addTrackingLog(employee.getId(), asset.getId(), newTrackingInput);

        assertNotNull(saved);
        assertEquals(200, saved.getId());
        assertEquals(TrackingAction.ALLOCATED, saved.getAction());
        assertEquals("New allocation", saved.getRemarks());
        assertEquals(employee, saved.getEmployee());
        assertEquals(asset, saved.getAsset());
        assertNotNull(saved.getTimestamp());
    }

    @Test
    void testGetAllTrackingLogs_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetTracking> trackingList = List.of(tracking);
        Page<AssetTracking> page = new PageImpl<>(trackingList, pageable, trackingList.size());

        when(assetTrackingRepo.findAll(pageable)).thenReturn(page);
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of(createExpectedAssetTrackingDTO(tracking)));

        List<AssetTrackingDTO> result = assetTrackingService.getAllTrackingLogs(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tracking.getAction().toString(), result.get(0).getAction());
    }

    @Test
    void testGetAllTrackingLogs_EmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetTracking> emptyTrackingList = Collections.emptyList();
        Page<AssetTracking> emptyPage = new PageImpl<>(emptyTrackingList, pageable, 0);

        when(assetTrackingRepo.findAll(pageable)).thenReturn(emptyPage);
        when(assetTrackingDTO.convertAssetTrackingToDto(emptyTrackingList)).thenReturn(Collections.emptyList());

        List<AssetTrackingDTO> result = assetTrackingService.getAllTrackingLogs(0, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByAction_Success() {
        List<AssetTracking> trackingList = List.of(tracking);
        when(assetTrackingRepo.findByAction(TrackingAction.ALLOCATED)).thenReturn(trackingList);
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of(createExpectedAssetTrackingDTO(tracking)));

        List<AssetTrackingDTO> result = assetTrackingService.getByAction("ALLOCATED");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ALLOCATED", result.get(0).getAction());
    }

    @Test
    void testGetByAction_InvalidActionString() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                assetTrackingService.getByAction("INVALID_ACTION"));

        assertTrue(ex.getMessage().contains("No enum constant com.springboot.assetsphere.enums.TrackingAction.INVALID_ACTION"));
    }

    @Test
    void testGetByAction_NoLogsFound() {
        when(assetTrackingRepo.findByAction(TrackingAction.RETURNED)).thenReturn(Collections.emptyList());
        when(assetTrackingDTO.convertAssetTrackingToDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AssetTrackingDTO> result = assetTrackingService.getByAction("RETURNED");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByUsername_Success() {
        List<AssetTracking> trackingList = List.of(tracking);
        when(assetTrackingRepo.findByEmployeeUserUsername("john@example.com")).thenReturn(trackingList);
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of(createExpectedAssetTrackingDTO(tracking)));

        List<AssetTrackingDTO> result = assetTrackingService.getByUsername("john@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("john@example.com", result.get(0).getUsername());
    }

    @Test
    void testGetByUsername_NoLogsFound() {
        when(assetTrackingRepo.findByEmployeeUserUsername("nonexistent@example.com")).thenReturn(Collections.emptyList());
        when(assetTrackingDTO.convertAssetTrackingToDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AssetTrackingDTO> result = assetTrackingService.getByUsername("nonexistent@example.com");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateByUsername_Success_PartialUpdate() throws ResourceNotFoundException {
        AssetTracking existingTrackingInRepo = new AssetTracking();
        existingTrackingInRepo.setId(100);
        existingTrackingInRepo.setAction(TrackingAction.ALLOCATED);
        existingTrackingInRepo.setRemarks("Initial remark");
        existingTrackingInRepo.setEmployee(employee);

        AssetTracking updatedTrackingInput = new AssetTracking();
        updatedTrackingInput.setRemarks("Updated remarks for John");

        when(assetTrackingRepo.findByEmployeeUserUsername("john@example.com")).thenReturn(List.of(existingTrackingInRepo));
        when(assetTrackingRepo.save(any(AssetTracking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssetTracking result = assetTrackingService.updateByUsername("john@example.com", updatedTrackingInput);

        assertNotNull(result);
        assertEquals(TrackingAction.ALLOCATED, result.getAction());
        assertEquals("Updated remarks for John", result.getRemarks());
        assertEquals(existingTrackingInRepo.getId(), result.getId());
    }

    @Test
    void testUpdateByUsername_Success_FullUpdate() throws ResourceNotFoundException {
        AssetTracking existingTrackingInRepo = new AssetTracking();
        existingTrackingInRepo.setId(100);
        existingTrackingInRepo.setAction(TrackingAction.ALLOCATED);
        existingTrackingInRepo.setRemarks("Initial remark");
        existingTrackingInRepo.setEmployee(employee);

        AssetTracking updatedTrackingInput = new AssetTracking();
        updatedTrackingInput.setAction(TrackingAction.RETURNED);
        updatedTrackingInput.setRemarks("Returned to store");

        when(assetTrackingRepo.findByEmployeeUserUsername("john@example.com")).thenReturn(List.of(existingTrackingInRepo));
        when(assetTrackingRepo.save(any(AssetTracking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssetTracking result = assetTrackingService.updateByUsername("john@example.com", updatedTrackingInput);

        assertNotNull(result);
        assertEquals(TrackingAction.RETURNED, result.getAction());
        assertEquals("Returned to store", result.getRemarks());
    }

    @Test
    void testUpdateByUsername_NotFound() {
        when(assetTrackingRepo.findByEmployeeUserUsername("unknown@example.com")).thenReturn(Collections.emptyList());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetTrackingService.updateByUsername("unknown@example.com", new AssetTracking()));

        assertEquals("Tracking not found for user: unknown@example.com", ex.getMessage());
    }

    @Test
    void testDeleteByUsername_Success() throws ResourceNotFoundException {
        List<AssetTracking> trackingList = List.of(tracking);
        when(assetTrackingRepo.findByEmployeeUserUsername("john@example.com")).thenReturn(trackingList);
        doNothing().when(assetTrackingRepo).delete(tracking);

        assetTrackingService.deleteByUsername("john@example.com");
    }

    @Test
    void testDeleteByUsername_NotFound() {
        when(assetTrackingRepo.findByEmployeeUserUsername("ghost@example.com")).thenReturn(Collections.emptyList());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
                assetTrackingService.deleteByUsername("ghost@example.com"));

        assertEquals("Tracking not found for user: ghost@example.com", ex.getMessage());
    }

    private AssetTrackingDTO createExpectedAssetTrackingDTO(AssetTracking tracking) {
        AssetTrackingDTO dto = new AssetTrackingDTO();
        dto.setUsername(tracking.getEmployee().getUser().getUsername());
        dto.setUserFullName(tracking.getEmployee().getName());
        dto.setJobTitle(tracking.getEmployee().getJobTitle());
        dto.setUserImageUrl(tracking.getEmployee().getImageUrl());
        dto.setAssetId(tracking.getAsset().getId());
        dto.setAssetName(tracking.getAsset().getAssetName());
        dto.setAssetModel(tracking.getAsset().getModel());
        dto.setAssetImageUrl(tracking.getAsset().getImageUrl());
        dto.setAction(tracking.getAction().toString());
        dto.setRemarks(tracking.getRemarks());
        return dto;
    }
    
    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
        employee = null;
        asset = null;
        tracking = null;
        user = null;
    }
}