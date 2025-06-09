package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.springboot.assetsphere.dto.AssetReturnRequestDTO;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.AssetReturnRequest;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetReturnRequestRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.service.AssetReturnRequestService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

@SpringBootTest
public class AssetReturnRequestServiceTest {

    @Mock private AssetReturnRequestRepository assetReturnRequestRepo;
    @Mock private EmployeeRepository employeeRepo;
    @Mock private AssetAllocationRepository assetAllocationRepo;
    @Mock private AssetReturnRequestDTO assetReturnRequestDTO;

    @InjectMocks
    private AssetReturnRequestService assetReturnRequestService;

    private Employee employee;
    private AssetAllocation allocation;
    private AssetReturnRequest returnRequest;
    private List<AssetReturnRequest> mockList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);

        allocation = new AssetAllocation();
        allocation.setId(10);

        returnRequest = new AssetReturnRequest();
        returnRequest.setId(100);
        returnRequest.setEmployee(employee);
        returnRequest.setAllocation(allocation);
        returnRequest.setRequestedAt(LocalDateTime.now());
        returnRequest.setStatus(RequestStatus.PENDING);

        mockList = List.of(returnRequest);
    }

    @AfterEach
    public void tearDown() {
        employee = null;
        allocation = null;
        returnRequest = null;
        mockList = null;
    }

    @Test
    public void testAddAssetReturnRequest() throws ResourceNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetAllocationRepo.findById(10)).thenReturn(Optional.of(allocation));
        when(assetReturnRequestRepo.save(any(AssetReturnRequest.class))).thenReturn(returnRequest);

        AssetReturnRequest savedRequest = assetReturnRequestService.addAssetReturnRequest(1, 10, new AssetReturnRequest());
        assertNotNull(savedRequest);
        verify(assetReturnRequestRepo, times(1)).save(any(AssetReturnRequest.class));
    }

    @Test
    public void testGetAllAssetReturnRequests() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<AssetReturnRequest> page = new PageImpl<>(mockList);

        when(assetReturnRequestRepo.findAll(pageable)).thenReturn(page);
        when(assetReturnRequestDTO.convertAssetReturnToDto(mockList)).thenReturn(List.of());

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getAllAssetReturnRequests(0, 3);
        assertNotNull(result);
    }

    @Test
    public void testGetByEmployeeId() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<AssetReturnRequest> page = new PageImpl<>(mockList);

        when(assetReturnRequestRepo.findByEmployeeId(1, pageable)).thenReturn(page);
        when(assetReturnRequestDTO.convertAssetReturnToDto(mockList)).thenReturn(List.of());

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getByEmployeeId(1, 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testGetByStatus() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<AssetReturnRequest> page = new PageImpl<>(mockList);

        when(assetReturnRequestRepo.findByStatus(RequestStatus.PENDING, pageable)).thenReturn(page);
        when(assetReturnRequestDTO.convertAssetReturnToDto(mockList)).thenReturn(List.of());

        List<AssetReturnRequestDTO> result = assetReturnRequestService.getByStatus("PENDING", 0, 2);
        assertNotNull(result);
    }

    @Test
    public void testAddAssetReturnRequest_EmployeeNotFound() {
        when(employeeRepo.findById(999)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
            assetReturnRequestService.addAssetReturnRequest(999, 10, returnRequest)
        );

        assertEquals("Employee Not Found, ID is invalid", ex.getMessage());
    }

    @Test
    public void testAddAssetReturnRequest_AllocationNotFound() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetAllocationRepo.findById(999)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
            assetReturnRequestService.addAssetReturnRequest(1, 999, returnRequest)
        );

        assertEquals("Allocation Not Found, ID is invalid", ex.getMessage());
    }
}