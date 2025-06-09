package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetReturnRequestDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetReturnRequest;
import com.springboot.assetsphere.service.AssetReturnRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assetreturnrequest")
public class AssetReturnRequestController {

    Logger logger = LoggerFactory.getLogger(AssetReturnRequestController.class);

    @Autowired
    private AssetReturnRequestService assetReturnRequestService;

    @PostMapping("/add/{employeeId}/{allocationId}")
    public ResponseEntity<?> addAssetReturnRequest(@PathVariable int employeeId,
                                                   @PathVariable int allocationId,
                                                   @RequestBody AssetReturnRequest request) throws ResourceNotFoundException {
        logger.info("Request to return asset from employeeId: " + employeeId + ", allocationId: " + allocationId);
        assetReturnRequestService.addAssetReturnRequest(employeeId, allocationId, request);
        logger.info("Asset return request added successfully");
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset Return Request Submitted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AssetReturnRequestDTO>> getAllAssetReturnRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all asset return requests, page: " + page + ", size: " + size);
        return ResponseEntity.ok(assetReturnRequestService.getAllAssetReturnRequests(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<AssetReturnRequestDTO>> getByEmployeeId(
            @PathVariable int employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching asset return requests for employeeId: " + employeeId);
        return ResponseEntity.ok(assetReturnRequestService.getByEmployeeId(employeeId, page, size));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetReturnRequestDTO>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching asset return requests by status: " + status);
        return ResponseEntity.ok(assetReturnRequestService.getByStatus(status, page, size));
    }
}