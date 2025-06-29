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
@CrossOrigin(origins = "http://localhost:5173")
public class LiquidAssetTransactionController {

    Logger logger = LoggerFactory.getLogger(LiquidAssetTransactionController.class);

    @Autowired
    private LiquidAssetTransactionService transactionService;

    @PostMapping("/add/{employeeId}/{requestId}")
    public ResponseEntity<?> createTransaction(@PathVariable int employeeId,
                                               @PathVariable int requestId,
                                               @RequestBody LiquidAssetTransaction transaction)
            throws ResourceNotFoundException {
        transactionService.createTransaction(employeeId, requestId, transaction);
        logger.info("Transaction created successfully for employeeId: {}", employeeId);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Liquid Asset Transaction Created Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LiquidAssetTransactionDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<LiquidAssetTransactionDTO>> getByEmployee(@PathVariable int employeeId) {
        return ResponseEntity.ok(transactionService.getByEmployeeId(employeeId));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<LiquidAssetTransactionDTO>> getByStatus(@PathVariable String status,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "1000000") int size)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(transactionService.getByStatus(status, page, size));
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<LiquidAssetTransaction> updateByUsername(@PathVariable String username,
                                                                   @RequestBody LiquidAssetTransaction transaction)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(transactionService.updateTransactionByUsername(username, transaction));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Map<String, String>> deleteByUsername(@PathVariable String username) throws ResourceNotFoundException {
        logger.info("Deleting transaction for username: {}", username);
        transactionService.deleteTransactionByUsername(username);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Transaction deleted successfully for username: " + username);
        return ResponseEntity.ok(response);
    }
}
