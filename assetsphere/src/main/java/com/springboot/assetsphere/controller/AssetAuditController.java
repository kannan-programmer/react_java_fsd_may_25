package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetAuditDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetAudit;
import com.springboot.assetsphere.service.AssetAuditService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assetaudit")
public class AssetAuditController {

    private final Logger logger = LoggerFactory.getLogger(AssetAuditController.class);

    @Autowired
    private AssetAuditService assetAuditService;

    @PostMapping("/add/{employeeId}/{assetId}")
    public ResponseEntity<?> createAssetAudit(@PathVariable int employeeId,
                                              @PathVariable int assetId,
                                              @RequestBody AssetAudit assetAudit) throws ResourceNotFoundException {
        logger.info("Creating asset audit for employeeId: " + employeeId + ", assetId: " + assetId);
        assetAuditService.createAssetAudit(employeeId, assetId, assetAudit);
        logger.info("Asset audit created");
        Map<String, String> map = new HashMap<>();
        map.put("message", "AssetAudit added successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetAuditDTO>> getAllAudits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all asset audits");
        return ResponseEntity.ok(assetAuditService.getAllAudits(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<AssetAuditDTO>> getAuditsByEmployee(
            @PathVariable int employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching asset audits by employeeId: " + employeeId);
        return ResponseEntity.ok(assetAuditService.getAuditsByEmployee(employeeId, page, size));
    }

    @GetMapping("/getByAsset/{assetId}")
    public ResponseEntity<List<AssetAuditDTO>> getAuditsByAsset(
            @PathVariable int assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching asset audits by assetId: " + assetId);
        return ResponseEntity.ok(assetAuditService.getAuditsByAsset(assetId, page, size));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetAuditDTO>> getAuditsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching asset audits by status: " + status);
        return ResponseEntity.ok(assetAuditService.getAuditsByStatus(status, page, size));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AssetAudit> getAuditById(@PathVariable int id) throws ResourceNotFoundException {
        logger.info("Fetching asset audit by id: " + id);
        return ResponseEntity.ok(assetAuditService.getAuditById(id));
    }
}