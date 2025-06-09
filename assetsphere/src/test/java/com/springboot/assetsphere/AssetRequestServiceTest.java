package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import com.springboot.assetsphere.dto.AssetRequestDTO;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetRequest;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.AssetRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.service.AssetRequestService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

@SpringBootTest
public class AssetRequestServiceTest {

    @Mock private AssetRequestRepository assetRequestRepo;
    @Mock private EmployeeRepository employeeRepo;
    @Mock private AssetRepository assetRepo;
    @Mock private AssetRequestDTO assetRequestDTO;

    @InjectMocks
    private AssetRequestService assetRequestService;

    private Employee employee;
    private Asset asset;
    private AssetRequest assetRequest;
    private List<AssetRequest> mockList;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);

        asset = new Asset();
        asset.setId(1);
        asset.setAssetName("Dell Laptop");

        assetRequest = new AssetRequest();
        assetRequest.setId(100);
        assetRequest.setEmployee(employee);
        assetRequest.setAsset(asset);
        assetRequest.setRequestedAt(LocalDateTime.now());
        assetRequest.setStatus(RequestStatus.PENDING);

        mockList = List.of(assetRequest);

        System.out.println("AssetRequest object created: " + assetRequest);
    }

    

    @Test
    public void testAddAssetRequest() throws ResourceNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(1)).thenReturn(Optional.of(asset));
        when(assetRequestRepo.save(any(AssetRequest.class))).thenReturn(assetRequest);

        AssetRequest savedRequest = assetRequestService.addAssetRequest(1, 1, new AssetRequest());
        assertNotNull(savedRequest);
        assertEquals("Dell Laptop", savedRequest.getAsset().getAssetName());
        verify(assetRequestRepo, times(1)).save(any(AssetRequest.class));
    }

    @Test
    public void testGetAllAssetRequest() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<AssetRequest> page = new PageImpl<>(mockList);

        when(assetRequestRepo.findAll(pageable)).thenReturn(page);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of());

        List<AssetRequestDTO> result = assetRequestService.getAllAssetRequest(0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetByEmployeeId() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<AssetRequest> page = new PageImpl<>(mockList);

        when(assetRequestRepo.findByEmployeeId(1, pageable)).thenReturn(page);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of());

        List<AssetRequestDTO> result = assetRequestService.getByEmployeeId(1, 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetByAssetId() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<AssetRequest> page = new PageImpl<>(mockList);

        when(assetRequestRepo.findByAssetId(1, pageable)).thenReturn(page);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of());

        List<AssetRequestDTO> result = assetRequestService.getByAssetId(1, 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetByStatus() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<AssetRequest> page = new PageImpl<>(mockList);

        when(assetRequestRepo.findByStatus(RequestStatus.PENDING, pageable)).thenReturn(page);
        when(assetRequestDTO.convertAssetRequestToDto(mockList)).thenReturn(List.of());

        List<AssetRequestDTO> result = assetRequestService.getByStatus("PENDING", 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testAddAssetRequest_EmployeeNotFound() {
        when(employeeRepo.findById(999)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
                assetRequestService.addAssetRequest(999, 1, assetRequest)
        );

        assertEquals("Employee Not Found, Id Given is Invalid!", ex.getMessage());
    }

    @Test
    public void testAddAssetRequest_AssetNotFound() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(999)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
                assetRequestService.addAssetRequest(1, 999, assetRequest)
        );

        assertEquals("Asset Not Found, Id Given is Invalid!", ex.getMessage());
    }
    @AfterEach
    public void destroy() {
        assetRequest = null;
        asset = null;
        employee = null;
        mockList = null;
        System.out.println("Test data cleared from memory.");
    }
}