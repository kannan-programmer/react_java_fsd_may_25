package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetAllocationDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.service.AssetAllocationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assetallocation")
public class AssetAllocationController {

    Logger logger = LoggerFactory.getLogger(AssetAllocationController.class);

    @Autowired
    private AssetAllocationService assetAllocationService;

    @PostMapping("/add/{employeeId}/{assetId}")
    public ResponseEntity<?> addAllocation(@PathVariable int employeeId,
                                           @PathVariable int assetId,
                                           @RequestBody AssetAllocation assetAllocation) throws ResourceNotFoundException {
        logger.info("Adding allocation for employeeId: " + employeeId + ", assetId: " + assetId);
        assetAllocationService.addAllocation(employeeId, assetId, assetAllocation);
        logger.info("Allocation added successfully");
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset Allocated Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetAllocationDTO>> getAllAllocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching all allocations, page: " + page + ", size: " + size);
        return ResponseEntity.ok(assetAllocationService.getAllAllocations(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<AssetAllocationDTO>> getByEmployeeId(
            @PathVariable int employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching allocations by employeeId: " + employeeId);
        return ResponseEntity.ok(assetAllocationService.getAllocationsByEmployee(employeeId, page, size));
    }

    @GetMapping("/getByAssetId/{assetId}")
    public ResponseEntity<List<AssetAllocationDTO>> getByAssetId(
            @PathVariable int assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching allocations by assetId: " + assetId);
        return ResponseEntity.ok(assetAllocationService.getAllocationsByAsset(assetId, page, size));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetAllocationDTO>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching allocations by status: " + status);
        return ResponseEntity.ok(assetAllocationService.getAllocationsByStatus(status, page, size));
    }
}