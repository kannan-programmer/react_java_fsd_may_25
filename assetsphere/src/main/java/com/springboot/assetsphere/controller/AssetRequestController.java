package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetRequestDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetRequest;
import com.springboot.assetsphere.service.AssetRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assetrequest")
public class AssetRequestController {

    Logger logger = LoggerFactory.getLogger(AssetRequestController.class);

    @Autowired
    private AssetRequestService assetRequestService;

    @PostMapping("/add/{employeeId}/{assetId}")
    public ResponseEntity<?> addAssetRequest(@PathVariable int employeeId,
                                             @PathVariable int assetId,
                                             @RequestBody AssetRequest assetRequest) throws ResourceNotFoundException {
        logger.info("Add request called with employeeId: " + employeeId + ", assetId: " + assetId);
        assetRequestService.addAssetRequest(employeeId, assetId, assetRequest);
        Map<String, String> map = new HashMap<>();
        map.put("message", "New Asset Requested Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetRequestDTO>> getAllAssetRequest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Get all requests called with page: " + page + ", size: " + size);
        return ResponseEntity.ok(assetRequestService.getAllAssetRequest(page, size));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AssetRequestDTO>> getByEmployeeId(
            @PathVariable int employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Get by employeeId called with id: " + employeeId);
        return ResponseEntity.ok(assetRequestService.getByEmployeeId(employeeId, page, size));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<AssetRequestDTO>> getByAssetId(
            @PathVariable int assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Get by assetId called with id: " + assetId);
        return ResponseEntity.ok(assetRequestService.getByAssetId(assetId, page, size));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AssetRequestDTO>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Get by status called with status: " + status);
        return ResponseEntity.ok(assetRequestService.getByStatus(status, page, size));
    }
}