package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.ServiceRequestDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.ServiceRequest;
import com.springboot.assetsphere.service.ServiceRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servicerequest")
public class ServiceRequestController {

    Logger logger = LoggerFactory.getLogger(ServiceRequestController.class);

    @Autowired
    private ServiceRequestService serviceRequestService;

    @PostMapping("/submit/{employeeId}/{assetId}")
    public ResponseEntity<?> submitRequest(@PathVariable int employeeId,
                                           @PathVariable int assetId,
                                           @RequestBody ServiceRequest request) throws ResourceNotFoundException {
        logger.info("Submitting service request for employeeId: " + employeeId + ", assetId: " + assetId);
        serviceRequestService.submitServiceRequest(employeeId, assetId, request);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Service Request Submitted Successfully");
        logger.info("Service request submitted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ServiceRequestDTO>> getAllServiceRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all service requests, page: " + page + ", size: " + size);
        return ResponseEntity.ok(serviceRequestService.getAllServiceRequests(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<ServiceRequestDTO>> getByEmployeeId(
            @PathVariable int employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching service requests by employeeId: " + employeeId);
        return ResponseEntity.ok(serviceRequestService.getByEmployeeId(employeeId, page, size));
    }

    @GetMapping("/getByAsset/{assetId}")
    public ResponseEntity<List<ServiceRequestDTO>> getByAssetId(
            @PathVariable int assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching service requests by assetId: " + assetId);
        return ResponseEntity.ok(serviceRequestService.getByAssetId(assetId, page, size));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<ServiceRequestDTO>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) throws ResourceNotFoundException {
        logger.info("Fetching service requests by status: " + status);
        return ResponseEntity.ok(serviceRequestService.getByStatus(status, page, size));
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<List<ServiceRequestDTO>> getByUserEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching service requests by email: " + email);
        return ResponseEntity.ok(serviceRequestService.getByUserEmail(email, page, size));
    }

    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<List<ServiceRequestDTO>> getByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching service requests by username: " + username);
        return ResponseEntity.ok(serviceRequestService.getByUsername(username, page, size));
    }
}