package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.LiquidAssetRequestDTO;
import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.LiquidAssetRequest;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.LiquidAssetRequestRepository;
import com.springboot.assetsphere.service.LiquidAssetRequestService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LiquidAssetRequestServiceTest {

    @Mock
    private LiquidAssetRequestRepository liquidAssetRequestRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private LiquidAssetRequestDTO liquidAssetRequestDTO;

    @InjectMocks
    private LiquidAssetRequestService liquidAssetRequestService;

    private AutoCloseable closeable;

    private Employee employee;
    private LiquidAssetRequest request;
    private LiquidAssetRequest savedRequest;
    private LiquidAssetRequestDTO dto;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);

        request = new LiquidAssetRequest();
        request.setItemName("Laptop");
        request.setItemCategory("Electronics");
        request.setPurchaseAmount(50000.0);
        request.setPurchaseDate(LocalDate.of(2024, 6, 1));
        request.setDocumentProofUrl("http://proof.com/doc.pdf");

        savedRequest = new LiquidAssetRequest();
        savedRequest.setId(100);
        savedRequest.setItemName("Laptop");
        savedRequest.setItemCategory("Electronics");
        savedRequest.setPurchaseAmount(50000.0);
        savedRequest.setPurchaseDate(LocalDate.of(2024, 6, 1));
        savedRequest.setDocumentProofUrl("http://proof.com/doc.pdf");
        savedRequest.setEmployee(employee);
        savedRequest.setSubmittedAt(LocalDateTime.now());
        savedRequest.setStatus(RequestStatus.PENDING);

        dto = new LiquidAssetRequestDTO();
        dto.setUsername("john");
        dto.setEmail("john@example.com");
        dto.setItemName("Laptop");
        dto.setItemCategory("Electronics");
        dto.setPurchaseAmount(50000.0);
        dto.setPurchaseDate(LocalDate.of(2024, 6, 1));
        dto.setDocumentProofUrl("http://proof.com/doc.pdf");
        dto.setStatus("PENDING");
        dto.setAdminComments(null);
    }

    

    @Test
    void testAddLiquidAssetRequest_Success() throws ResourceNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(liquidAssetRequestRepo.save(any(LiquidAssetRequest.class))).thenReturn(savedRequest);

        LiquidAssetRequest result = liquidAssetRequestService.addLiquidAssetRequest(1, request);

        assertNotNull(result);
        assertEquals("Laptop", result.getItemName());
        assertEquals(RequestStatus.PENDING, result.getStatus());
        verify(employeeRepo).findById(1);
        verify(liquidAssetRequestRepo).save(request);
    }

    @Test
    void testAddLiquidAssetRequest_EmployeeNotFound() {
        when(employeeRepo.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            liquidAssetRequestService.addLiquidAssetRequest(99, request);
        });

        assertEquals("Employee not found with ID: 99", ex.getMessage());
    }

    @Test
    void testGetAllLiquidAssetRequests() {
        Pageable pageable = PageRequest.of(0, 5);
        List<LiquidAssetRequest> requests = List.of(savedRequest);
        when(liquidAssetRequestRepo.findAll(pageable)).thenReturn(new PageImpl<>(requests));
        when(liquidAssetRequestDTO.convertLiquidAssetRequestToDto(requests)).thenReturn(List.of(dto));

        List<LiquidAssetRequestDTO> result = liquidAssetRequestService.getAllLiquidAssetRequests(0, 5);

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getItemName());
    }

    @Test
    void testGetByEmployeeId() {
        Pageable pageable = PageRequest.of(0, 5);
        List<LiquidAssetRequest> requests = List.of(savedRequest);
        when(liquidAssetRequestRepo.findByEmployeeId(1, pageable)).thenReturn(new PageImpl<>(requests));
        when(liquidAssetRequestDTO.convertLiquidAssetRequestToDto(requests)).thenReturn(List.of(dto));

        List<LiquidAssetRequestDTO> result = liquidAssetRequestService.getByEmployeeId(1, 0, 5);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByStatus_Valid() throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(0, 5);
        List<LiquidAssetRequest> requests = List.of(savedRequest);
        when(liquidAssetRequestRepo.findByStatus(RequestStatus.PENDING, pageable)).thenReturn(new PageImpl<>(requests));
        when(liquidAssetRequestDTO.convertLiquidAssetRequestToDto(requests)).thenReturn(List.of(dto));

        List<LiquidAssetRequestDTO> result = liquidAssetRequestService.getByStatus("PENDING", 0, 5);

        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }

    @Test
    void testGetByStatus_Invalid() {
        assertThrows(ResourceNotFoundException.class, () -> {
            liquidAssetRequestService.getByStatus("INVALID_STATUS", 0, 5);
        });
    }

    @Test
    void testGetByUserEmail() {
        Pageable pageable = PageRequest.of(0, 5);
        List<LiquidAssetRequest> requests = List.of(savedRequest);
        when(liquidAssetRequestRepo.findByEmployeeUserEmail("john@example.com", pageable)).thenReturn(new PageImpl<>(requests));
        when(liquidAssetRequestDTO.convertLiquidAssetRequestToDto(requests)).thenReturn(List.of(dto));

        List<LiquidAssetRequestDTO> result = liquidAssetRequestService.getByUserEmail("john@example.com", 0, 5);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByUsername() {
        Pageable pageable = PageRequest.of(0, 5);
        List<LiquidAssetRequest> requests = List.of(savedRequest);
        when(liquidAssetRequestRepo.findByEmployeeUserUsername("john", pageable)).thenReturn(new PageImpl<>(requests));
        when(liquidAssetRequestDTO.convertLiquidAssetRequestToDto(requests)).thenReturn(List.of(dto));

        List<LiquidAssetRequestDTO> result = liquidAssetRequestService.getByUsername("john", 0, 5);

        assertEquals(1, result.size());
    }
    
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}