package com.springboot.assetsphere.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.dto.LiquidAssetTransactionDTO;
import com.springboot.assetsphere.enums.PaymentStatus;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.LiquidAssetRequest;
import com.springboot.assetsphere.model.LiquidAssetTransaction;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.LiquidAssetRequestRepository;
import com.springboot.assetsphere.repository.LiquidAssetTransactionRepository;

@Service
public class LiquidAssetTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(LiquidAssetTransactionService.class);

    private final LiquidAssetTransactionRepository transactionRepo;
    private final EmployeeRepository employeeRepo;
    private final LiquidAssetRequestRepository requestRepo;
    private final LiquidAssetTransactionDTO transactionDTO;

    public LiquidAssetTransactionService(LiquidAssetTransactionRepository transactionRepo,
                                         EmployeeRepository employeeRepo,
                                         LiquidAssetRequestRepository requestRepo,
                                         LiquidAssetTransactionDTO transactionDTO) {
        this.transactionRepo = transactionRepo;
        this.employeeRepo = employeeRepo;
        this.requestRepo = requestRepo;
        this.transactionDTO = transactionDTO;
    }

    public LiquidAssetTransaction createTransaction(int employeeId, int requestId, LiquidAssetTransaction transaction)
            throws ResourceNotFoundException {
        logger.info("Creating liquid asset transaction for employeeId {} and requestId {}", employeeId, requestId);

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Employee not found with ID {}", employeeId);
                    return new ResourceNotFoundException("Employee not found");
                });
        LiquidAssetRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> {
                    logger.error("Liquid asset request not found with ID {}", requestId);
                    return new ResourceNotFoundException("Liquid asset request not found");
                });

        transaction.setEmployee(employee);
        transaction.setLiquidAssetRequest(request);

        LiquidAssetTransaction savedTransaction = transactionRepo.save(transaction);
        logger.info("Liquid asset transaction saved with id {}", savedTransaction.getId());

        return savedTransaction;
    }

    public List<LiquidAssetTransactionDTO> getAllTransactions(int page, int size) {
        logger.info("Fetching all liquid asset transactions - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return transactionDTO.convertLiquidTransactionToDto(transactionRepo.findAll(pageable).getContent());
    }

    public List<LiquidAssetTransactionDTO> getByEmployeeId(int employeeId, int page, int size) {
        logger.info("Fetching liquid asset transactions by employee id {}, page: {}, size: {}", employeeId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return transactionDTO.convertLiquidTransactionToDto(transactionRepo.findByEmployeeId(employeeId, pageable).getContent());
    }

    public List<LiquidAssetTransactionDTO> getByStatus(String status, int page, int size) throws ResourceNotFoundException {
        logger.info("Fetching liquid asset transactions by status {}, page: {}, size: {}", status, page, size);

        PaymentStatus enumStatus;
        try {
            enumStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment status provided: {}", status);
            throw new ResourceNotFoundException("Invalid payment status: " + status);
        }
        Pageable pageable = PageRequest.of(page, size);
        return transactionDTO.convertLiquidTransactionToDto(transactionRepo.findByStatus(enumStatus, pageable).getContent());
    }
}