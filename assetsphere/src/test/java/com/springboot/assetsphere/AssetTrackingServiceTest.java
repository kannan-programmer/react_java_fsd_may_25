package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.AssetTrackingDTO;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetTracking;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetTrackingRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.service.AssetTrackingService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setUsername("john123");
        user.setEmail("john@example.com");

        employee = new Employee();
        employee.setId(1);
        employee.setUser(user);

        asset = new Asset();
        asset.setId(10);
        asset.setAssetName("Laptop");

        tracking = new AssetTracking();
        tracking.setId(100);
        tracking.setAction(RequestStatus.PENDING);
        tracking.setEmployee(employee);
        tracking.setAsset(asset);
        tracking.setTimestamp(LocalDateTime.now());
    }

    

    @Test
    void testAddTrackingLog_Success() throws ResourceNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(10)).thenReturn(Optional.of(asset));
        when(assetTrackingRepo.save(any(AssetTracking.class))).thenReturn(tracking);

        AssetTracking saved = assetTrackingService.addTrackingLog(1, 10, new AssetTracking());

        assertNotNull(saved);
        assertEquals(RequestStatus.PENDING, saved.getAction());
        assertEquals(employee, saved.getEmployee());
        verify(assetTrackingRepo, times(1)).save(any(AssetTracking.class));
    }

    @Test
    void testAddTrackingLog_EmployeeNotFound() {
        when(employeeRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            assetTrackingService.addTrackingLog(999, 10, new AssetTracking())
        );

        assertEquals("Employee not found with ID: 999", exception.getMessage());
    }

    @Test
    void testGetAllTrackingLogs() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetTracking> trackingList = List.of(tracking);
        when(assetTrackingRepo.findAll(pageable)).thenReturn(new PageImpl<>(trackingList));
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of());

        List<AssetTrackingDTO> result = assetTrackingService.getAllTrackingLogs(0, 10);

        assertNotNull(result);
        verify(assetTrackingRepo).findAll(pageable);
    }

    @Test
    void testGetByUsername() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetTracking> trackingList = List.of(tracking);
        when(assetTrackingRepo.findByEmployeeUserUsername("john123", pageable)).thenReturn(new PageImpl<>(trackingList));
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of());

        List<AssetTrackingDTO> result = assetTrackingService.getByUsername("john123", 0, 10);

        assertNotNull(result);
        verify(assetTrackingRepo).findByEmployeeUserUsername("john123", pageable);
    }

    @Test
    void testGetByAction() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetTracking> trackingList = Arrays.asList(tracking);
        when(assetTrackingRepo.findByAction(RequestStatus.PENDING, pageable)).thenReturn(new PageImpl<>(trackingList));
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of());

        List<AssetTrackingDTO> result = assetTrackingService.getByAction("PENDING", 0, 10);

        assertNotNull(result);
        verify(assetTrackingRepo).findByAction(RequestStatus.PENDING, pageable);
    }

    @Test
    void testGetByEmployeeId() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetTracking> trackingList = List.of(tracking);
        when(assetTrackingRepo.findByEmployeeId(1, pageable)).thenReturn(new PageImpl<>(trackingList));
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of());

        List<AssetTrackingDTO> result = assetTrackingService.getByEmployeeId(1, 0, 10);

        assertNotNull(result);
        verify(assetTrackingRepo).findByEmployeeId(1, pageable);
    }

    @Test
    void testGetByAssetId() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetTracking> trackingList = List.of(tracking);
        when(assetTrackingRepo.findByAssetId(10, pageable)).thenReturn(new PageImpl<>(trackingList));
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of());

        List<AssetTrackingDTO> result = assetTrackingService.getByAssetId(10, 0, 10);

        assertNotNull(result);
        verify(assetTrackingRepo).findByAssetId(10, pageable);
    }

    @Test
    void testGetByUserEmail() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AssetTracking> trackingList = List.of(tracking);
        when(assetTrackingRepo.findByEmployeeUserEmail("john@example.com", pageable)).thenReturn(new PageImpl<>(trackingList));
        when(assetTrackingDTO.convertAssetTrackingToDto(trackingList)).thenReturn(List.of());

        List<AssetTrackingDTO> result = assetTrackingService.getByUserEmail("john@example.com", 0, 10);

        assertNotNull(result);
        verify(assetTrackingRepo).findByEmployeeUserEmail("john@example.com", pageable);
    }
    @AfterEach
    void tearDown() throws Exception {
        closeable.close(); // Clean up mocks
    }
}