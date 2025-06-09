package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.LiquidAssetTransactionDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.LiquidAssetTransaction;
import com.springboot.assetsphere.service.LiquidAssetTransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liquidassettransaction")
public class LiquidAssetTransactionController {

    Logger logger = LoggerFactory.getLogger(LiquidAssetTransactionController.class);

    @Autowired
    private LiquidAssetTransactionService transactionService;

    @PostMapping("/add/{employeeId}/{requestId}")
    public ResponseEntity<?> createTransaction(@PathVariable int employeeId,
                                               @PathVariable int requestId,
                                               @RequestBody LiquidAssetTransaction transaction)
            throws ResourceNotFoundException {
        logger.info("Creating transaction for employeeId: " + employeeId + ", requestId: " + requestId);
        transactionService.createTransaction(employeeId, requestId, transaction);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Liquid Asset Transaction Created Successfully");
        logger.info("Transaction created successfully for employeeId: " + employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LiquidAssetTransactionDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all transactions, page: " + page + ", size: " + size);
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<LiquidAssetTransactionDTO>> getByEmployee(
            @PathVariable int employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching transactions by employeeId: " + employeeId);
        return ResponseEntity.ok(transactionService.getByEmployeeId(employeeId, page, size));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<LiquidAssetTransactionDTO>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) throws ResourceNotFoundException {
        logger.info("Fetching transactions by status: " + status);
        return ResponseEntity.ok(transactionService.getByStatus(status, page, size));
    }
}