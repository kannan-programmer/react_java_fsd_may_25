package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.ServiceRequestDTO;
import com.springboot.assetsphere.enums.ServiceStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.ServiceRequest;
import com.springboot.assetsphere.repository.AssetRepository;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.ServiceRequestRepository;
import com.springboot.assetsphere.service.ServiceRequestService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class ServiceRequestServiceTest {

    @Mock
    private ServiceRequestRepository serviceRequestRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private AssetRepository assetRepo;

    @Mock
    private ServiceRequestDTO serviceRequestDTO;

    @InjectMocks
    private ServiceRequestService serviceRequestService;

    private AutoCloseable closeable;

    private Employee employee;
    private Asset asset;
    private ServiceRequest request;
    private ServiceRequest savedRequest;
    private ServiceRequestDTO dto;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);

        asset = new Asset();
        asset.setId(100);

        request = new ServiceRequest();
        request.setDescription("Fix Monitor");

        savedRequest = new ServiceRequest();
        savedRequest.setId(500);
        savedRequest.setDescription("Fix Monitor");
        savedRequest.setEmployee(employee);
        savedRequest.setAsset(asset);
        savedRequest.setStatus(ServiceStatus.IN_PROGRESS);

        dto = new ServiceRequestDTO();
        dto.setDescription("Fix Monitor");
        dto.setStatus("IN_PROGRESS");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSubmitServiceRequest_Success() throws ResourceNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(100)).thenReturn(Optional.of(asset));
        when(serviceRequestRepo.save(request)).thenReturn(savedRequest);

        ServiceRequest result = serviceRequestService.submitServiceRequest(1, 100, request);

        assertNotNull(result);
        assertEquals("Fix Monitor", result.getDescription());
        assertEquals(ServiceStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void testSubmitServiceRequest_EmployeeNotFound() {
        when(employeeRepo.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            serviceRequestService.submitServiceRequest(99, 100, request);
        });

        assertTrue(ex.getMessage().contains("Employee not found"));
    }

    @Test
    void testSubmitServiceRequest_AssetNotFound() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(assetRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            serviceRequestService.submitServiceRequest(1, 999, request);
        });

        assertTrue(ex.getMessage().contains("Asset not found"));
    }

    @Test
    void testGetAllServiceRequests() {
        Pageable pageable = PageRequest.of(0, 5);
        List<ServiceRequest> requests = List.of(savedRequest);
        when(serviceRequestRepo.findAll(pageable)).thenReturn(new PageImpl<>(requests));
        when(serviceRequestDTO.convertServiceRequestToDto(requests)).thenReturn(List.of(dto));

        List<ServiceRequestDTO> result = serviceRequestService.getAllServiceRequests(0, 5);

        assertEquals(1, result.size());
        assertEquals("IN_PROGRESS", result.get(0).getStatus());
    }

    @Test
    void testGetByEmployeeId() {
        Pageable pageable = PageRequest.of(0, 5);
        List<ServiceRequest> requests = List.of(savedRequest);
        when(serviceRequestRepo.findByEmployeeId(1, pageable)).thenReturn(new PageImpl<>(requests));
        when(serviceRequestDTO.convertServiceRequestToDto(requests)).thenReturn(List.of(dto));

        List<ServiceRequestDTO> result = serviceRequestService.getByEmployeeId(1, 0, 5);

        assertEquals(1, result.size());
        assertEquals("Fix Monitor", result.get(0).getDescription());
    }

    @Test
    void testGetByAssetId() {
        Pageable pageable = PageRequest.of(0, 5);
        List<ServiceRequest> requests = List.of(savedRequest);
        when(serviceRequestRepo.findByAssetId(100, pageable)).thenReturn(new PageImpl<>(requests));
        when(serviceRequestDTO.convertServiceRequestToDto(requests)).thenReturn(List.of(dto));

        List<ServiceRequestDTO> result = serviceRequestService.getByAssetId(100, 0, 5);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByStatus() {
        Pageable pageable = PageRequest.of(0, 5);
        List<ServiceRequest> requests = List.of(savedRequest);
        when(serviceRequestRepo.findByStatus(ServiceStatus.IN_PROGRESS, pageable)).thenReturn(new PageImpl<>(requests));
        when(serviceRequestDTO.convertServiceRequestToDto(requests)).thenReturn(List.of(dto));

        List<ServiceRequestDTO> result = serviceRequestService.getByStatus("IN_PROGRESS", 0, 5);

        assertEquals(1, result.size());
        assertEquals("IN_PROGRESS", result.get(0).getStatus());
    }

    @Test
    void testGetByUserEmail() {
        Pageable pageable = PageRequest.of(0, 5);
        List<ServiceRequest> requests = List.of(savedRequest);
        when(serviceRequestRepo.findByEmployeeUserEmail("user@example.com", pageable)).thenReturn(new PageImpl<>(requests));
        when(serviceRequestDTO.convertServiceRequestToDto(requests)).thenReturn(List.of(dto));

        List<ServiceRequestDTO> result = serviceRequestService.getByUserEmail("user@example.com", 0, 5);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByUsername() {
        Pageable pageable = PageRequest.of(0, 5);
        List<ServiceRequest> requests = List.of(savedRequest);
        when(serviceRequestRepo.findByEmployeeUserUsername("gopal", pageable)).thenReturn(new PageImpl<>(requests));
        when(serviceRequestDTO.convertServiceRequestToDto(requests)).thenReturn(List.of(dto));

        List<ServiceRequestDTO> result = serviceRequestService.getByUsername("gopal", 0, 5);

        assertEquals(1, result.size());
    }
}