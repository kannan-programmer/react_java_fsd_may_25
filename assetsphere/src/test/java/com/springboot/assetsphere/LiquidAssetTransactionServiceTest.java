package com.springboot.assetsphere;

import com.springboot.assetsphere.dto.LiquidAssetTransactionDTO;
import com.springboot.assetsphere.enums.PaymentStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.LiquidAssetRequest;
import com.springboot.assetsphere.model.LiquidAssetTransaction;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.LiquidAssetRequestRepository;
import com.springboot.assetsphere.repository.LiquidAssetTransactionRepository;
import com.springboot.assetsphere.service.LiquidAssetTransactionService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LiquidAssetTransactionServiceTest {

    @Mock
    private LiquidAssetTransactionRepository transactionRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private LiquidAssetRequestRepository requestRepo;

    @Mock
    private LiquidAssetTransactionDTO transactionDTO;

    @InjectMocks
    private LiquidAssetTransactionService transactionService;

    private AutoCloseable closeable;

    private Employee employee;
    private LiquidAssetRequest request;
    private LiquidAssetTransaction transaction;
    private LiquidAssetTransaction savedTransaction;
    private LiquidAssetTransactionDTO dto;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);

        request = new LiquidAssetRequest();
        request.setId(10);

        transaction = new LiquidAssetTransaction();
        transaction.setAmountPaid(1000.0);

        savedTransaction = new LiquidAssetTransaction();
        savedTransaction.setId(101);
        savedTransaction.setAmountPaid(1000.0);
        savedTransaction.setEmployee(employee);
        savedTransaction.setLiquidAssetRequest(request);
        savedTransaction.setStatus(PaymentStatus.PAID);

        dto = new LiquidAssetTransactionDTO();
        dto.setAmountPaid(1000.0);
        dto.setStatus("PAID");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testCreateTransaction_Success() throws ResourceNotFoundException {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(requestRepo.findById(10)).thenReturn(Optional.of(request));
        when(transactionRepo.save(transaction)).thenReturn(savedTransaction);

        LiquidAssetTransaction result = transactionService.createTransaction(1, 10, transaction);

        assertNotNull(result);
        assertEquals(1000.0, result.getAmountPaid());
        assertEquals(PaymentStatus.PAID, result.getStatus());
        verify(employeeRepo).findById(1);
        verify(requestRepo).findById(10);
        verify(transactionRepo).save(transaction);
    }

    @Test
    void testCreateTransaction_EmployeeNotFound() {
        when(employeeRepo.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(99, 10, transaction);
        });

        assertEquals("Employee not found", ex.getMessage());
    }

    @Test
    void testCreateTransaction_RequestNotFound() {
        when(employeeRepo.findById(1)).thenReturn(Optional.of(employee));
        when(requestRepo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(1, 999, transaction);
        });

        assertEquals("Liquid asset request not found", ex.getMessage());
    }

    @Test
    void testGetAllTransactions() {
        Pageable pageable = PageRequest.of(0, 5);
        List<LiquidAssetTransaction> transactions = List.of(savedTransaction);
        when(transactionRepo.findAll(pageable)).thenReturn(new PageImpl<>(transactions));
        when(transactionDTO.convertLiquidTransactionToDto(transactions)).thenReturn(List.of(dto));

        List<LiquidAssetTransactionDTO> result = transactionService.getAllTransactions(0, 5);

        assertEquals(1, result.size());
        assertEquals(1000.0, result.get(0).getAmountPaid());
    }

    @Test
    void testGetByEmployeeId() {
        Pageable pageable = PageRequest.of(0, 5);
        List<LiquidAssetTransaction> transactions = List.of(savedTransaction);
        when(transactionRepo.findByEmployeeId(1, pageable)).thenReturn(new PageImpl<>(transactions));
        when(transactionDTO.convertLiquidTransactionToDto(transactions)).thenReturn(List.of(dto));

        List<LiquidAssetTransactionDTO> result = transactionService.getByEmployeeId(1, 0, 5);

        assertEquals(1, result.size());
        assertEquals("PAID", result.get(0).getStatus());
    }

    @Test
    void testGetByStatus_Valid() throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(0, 5);
        List<LiquidAssetTransaction> transactions = List.of(savedTransaction);
        when(transactionRepo.findByStatus(PaymentStatus.PAID, pageable)).thenReturn(new PageImpl<>(transactions));
        when(transactionDTO.convertLiquidTransactionToDto(transactions)).thenReturn(List.of(dto));

        List<LiquidAssetTransactionDTO> result = transactionService.getByStatus("PAID", 0, 5);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByStatus_Invalid() {
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getByStatus("INVALID_STATUS", 0, 5);
        });
    }
}