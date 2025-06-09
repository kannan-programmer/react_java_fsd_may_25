package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.springboot.assetsphere.dto.AssetAllocationDTO;
import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAllocationRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.service.AssetAllocationService;

import org.junit.jupiter.api.*;

import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

@SpringBootTest
public class AssetAllocationServiceTest {

    @Mock
    private AssetAllocationRepository assetAllocationRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private AssetRepository assetRepo;

    @Mock
    private AssetAllocationDTO assetAllocationDTO;

    @InjectMocks
    private AssetAllocationService assetAllocationService;

    private Employee employee;
    private Asset asset;
    private AssetAllocation allocation;
    private AssetAllocationDTO dto;

    @BeforeEach 
    public void init() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);
        System.out.println("Employee object created: " + employee);

        asset = new Asset();
        asset.setId(1);
        System.out.println("Asset object created: " + asset);

        allocation = new AssetAllocation();
        allocation.setId(1);
        allocation.setEmployee(employee);
        allocation.setAsset(asset);
        allocation.setAllocatedAt(LocalDate.now());
        System.out.println("AssetAllocation object created: " + allocation);

        dto = new AssetAllocationDTO();
    }


    @Test
    public void testAddAllocation() throws ResourceNotFoundException {
        int employeeId = 1;
        int assetId = 1;

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(assetId)).thenReturn(Optional.of(asset));
        when(assetAllocationRepo.save(any(AssetAllocation.class))).thenReturn(allocation);

        AssetAllocation result = assetAllocationService.addAllocation(employeeId, assetId, allocation);

        assertEquals(allocation, result);
        verify(assetAllocationRepo).save(allocation);
    }

    @Test
    public void testGetAllAllocations() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAllocation> list = Arrays.asList(allocation);
        Page<AssetAllocation> page = new PageImpl<>(list);

        when(assetAllocationRepo.findAll(pageable)).thenReturn(page);
        when(assetAllocationDTO.convertAssetAllocationToDto(list)).thenReturn(Arrays.asList(dto));

        List<AssetAllocationDTO> result = assetAllocationService.getAllAllocations(0, 5);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllocationsByEmployee() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAllocation> list = Arrays.asList(allocation);
        Page<AssetAllocation> page = new PageImpl<>(list);

        when(assetAllocationRepo.findByEmployeeId(employee.getId(), pageable)).thenReturn(page);
        when(assetAllocationDTO.convertAssetAllocationToDto(list)).thenReturn(Arrays.asList(dto));

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByEmployee(employee.getId(), 0, 5);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllocationsByAsset() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAllocation> list = Arrays.asList(allocation);
        Page<AssetAllocation> page = new PageImpl<>(list);

        when(assetAllocationRepo.findByAssetId(asset.getId(), pageable)).thenReturn(page);
        when(assetAllocationDTO.convertAssetAllocationToDto(list)).thenReturn(Arrays.asList(dto));

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByAsset(asset.getId(), 0, 5);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllocationsByStatus() {
        String status = "ASSIGNED";
        AllocationStatus allocationStatus = AllocationStatus.ASSIGNED;
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAllocation> list = Arrays.asList(allocation);
        Page<AssetAllocation> page = new PageImpl<>(list);

        when(assetAllocationRepo.findByStatus(allocationStatus, pageable)).thenReturn(page);
        when(assetAllocationDTO.convertAssetAllocationToDto(list)).thenReturn(Arrays.asList(dto));

        List<AssetAllocationDTO> result = assetAllocationService.getAllocationsByStatus(status, 0, 5);

        assertEquals(1, result.size());
    }
    
    @AfterEach // After each test case, the objects used in them will get nullified and HEAP memory will be free
    public void afterTest() {
        employee = null;
        System.out.println("Employee object released.. " + employee);
        asset = null;
        System.out.println("Asset object released.. " + asset);
        allocation = null;
        System.out.println("AssetAllocation object released.. " + allocation);
        dto = null;
        System.out.println("DTO object released.. " + dto);
    }
}