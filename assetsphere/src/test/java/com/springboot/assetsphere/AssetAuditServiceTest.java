package com.springboot.assetsphere;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import com.springboot.assetsphere.dto.AssetAuditDTO;
import com.springboot.assetsphere.enums.AuditStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetAudit;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.repository.AssetAuditRepository;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
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

    @InjectMocks
    private AssetAuditService assetAuditService;

    private AssetAudit assetAudit;
    private Asset asset;
    private Employee employee;
    private AssetAuditDTO dto;

    @BeforeEach // before every test these objects will be created at locations in HEAP
    public void init() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);
        System.out.println("Employee object created: " + employee);

        asset = new Asset();
        asset.setId(1);
        System.out.println("Asset object created: " + asset);

        assetAudit = new AssetAudit();
        assetAudit.setId(100);
        assetAudit.setEmployee(employee);
        assetAudit.setAsset(asset);
        assetAudit.setAuditedAt(LocalDateTime.now());
        System.out.println("AssetAudit object created: " + assetAudit);

        dto = new AssetAuditDTO();
    }

    

    @Test
    public void testCreateAssetAudit() throws ResourceNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(1)).thenReturn(Optional.of(asset));
        when(assetAuditRepo.save(any(AssetAudit.class))).thenReturn(assetAudit);

        AssetAudit result = assetAuditService.createAssetAudit(1, 1, assetAudit);

        assertEquals(assetAudit, result);
        verify(assetAuditRepo).save(assetAudit);
    }

    @Test
    public void testGetAllAudits() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAudit> audits = Arrays.asList(assetAudit);
        Page<AssetAudit> page = new PageImpl<>(audits);

        when(assetAuditRepo.findAll(pageable)).thenReturn(page);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(Arrays.asList(dto));

        List<AssetAuditDTO> result = assetAuditService.getAllAudits(0, 5);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAuditsByEmployee() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAudit> audits = Arrays.asList(assetAudit);
        Page<AssetAudit> page = new PageImpl<>(audits);

        when(assetAuditRepo.findByEmployeeId(1, pageable)).thenReturn(page);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(Arrays.asList(dto));

        List<AssetAuditDTO> result = assetAuditService.getAuditsByEmployee(1, 0, 5);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAuditsByAsset() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAudit> audits = Arrays.asList(assetAudit);
        Page<AssetAudit> page = new PageImpl<>(audits);

        when(assetAuditRepo.findByAssetId(1, pageable)).thenReturn(page);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(Arrays.asList(dto));

        List<AssetAuditDTO> result = assetAuditService.getAuditsByAsset(1, 0, 5);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAuditsByStatus() {
        Pageable pageable = PageRequest.of(0, 5);
        List<AssetAudit> audits = Arrays.asList(assetAudit);
        Page<AssetAudit> page = new PageImpl<>(audits);
        AuditStatus status = AuditStatus.PENDING;

        when(assetAuditRepo.findByStatus(status, pageable)).thenReturn(page);
        when(assetAuditDTO.convertAssetAuditToDto(audits)).thenReturn(Arrays.asList(dto));

        List<AssetAuditDTO> result = assetAuditService.getAuditsByStatus("PENDING", 0, 5);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAuditById() throws ResourceNotFoundException {
        when(assetAuditRepo.findById(100)).thenReturn(Optional.of(assetAudit));
        AssetAudit result = assetAuditService.getAuditById(100);
        assertEquals(assetAudit, result);
    }

    @Test
    public void testGetAuditById_NotFound() {
        when(assetAuditRepo.findById(999)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            assetAuditService.getAuditById(999);
        });

        assertEquals("Audit not found", ex.getMessage());
    }
    
    @AfterEach // After each test, objects will get nullified and memory will be free
    public void destroy() {
        employee = null;
        System.out.println("Employee object released: " + employee);
        asset = null;
        System.out.println("Asset object released: " + asset);
        assetAudit = null;
        System.out.println("AssetAudit object released: " + assetAudit);
        dto = null;
        System.out.println("DTO object released: " + dto);
    }
}