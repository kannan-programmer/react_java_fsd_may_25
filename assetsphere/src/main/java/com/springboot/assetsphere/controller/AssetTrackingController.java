package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetTrackingDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetTracking;
import com.springboot.assetsphere.service.AssetTrackingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assettracking")
public class AssetTrackingController {

    Logger logger = LoggerFactory.getLogger(AssetTrackingController.class);

    @Autowired
    private AssetTrackingService assetTrackingService;

    @PostMapping("/add/{employeeId}/{assetId}")
    public ResponseEntity<?> addTrackingLog(@PathVariable int employeeId,
                                            @PathVariable int assetId,
                                            @RequestBody AssetTracking tracking) throws ResourceNotFoundException {
        logger.info("Adding tracking log for employeeId: " + employeeId + ", assetId: " + assetId);
        assetTrackingService.addTrackingLog(employeeId, assetId, tracking);
        logger.info("Asset tracking log added successfully");
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset Tracking Added Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetTrackingDTO>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all asset tracking logs");
        return ResponseEntity.ok(assetTrackingService.getAllTrackingLogs(page, size));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AssetTrackingDTO>> getByEmployeeId(
            @PathVariable int employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching tracking logs by employeeId: " + employeeId);
        return ResponseEntity.ok(assetTrackingService.getByEmployeeId(employeeId, page, size));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<AssetTrackingDTO>> getByAssetId(
            @PathVariable int assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching tracking logs by assetId: " + assetId);
        return ResponseEntity.ok(assetTrackingService.getByAssetId(assetId, page, size));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetTrackingDTO>> getByAction(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching tracking logs by status: " + status);
        return ResponseEntity.ok(assetTrackingService.getByAction(status, page, size));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<AssetTrackingDTO>> getByEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching tracking logs by email: " + email);
        return ResponseEntity.ok(assetTrackingService.getByUserEmail(email, page, size));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<AssetTrackingDTO>> getByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching tracking logs by username: " + username);
        return ResponseEntity.ok(assetTrackingService.getByUsername(username, page, size));
    }
}