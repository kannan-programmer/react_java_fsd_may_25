package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.ServiceRequestDTO;
import com.springboot.assetsphere.exception.AssetNotFoundException;
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
@CrossOrigin(origins = "http://localhost:5173")
public class ServiceRequestController {

    Logger logger = LoggerFactory.getLogger(ServiceRequestController.class);

    @Autowired
    private ServiceRequestService serviceRequestService;

    @PostMapping("/submit/{employeeId}/{assetId}")
    public ResponseEntity<?> submitRequest(@PathVariable int employeeId, @PathVariable int assetId,
                                           @RequestBody ServiceRequest request) throws ResourceNotFoundException, AssetNotFoundException {
        serviceRequestService.submitServiceRequest(employeeId, assetId, request);
        logger.info("Service request submitted successfully for employee ID: {}, asset ID: {}", employeeId, assetId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Service Request Submitted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ServiceRequestDTO>> getAllServiceRequest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        return ResponseEntity.ok(serviceRequestService.getAllServiceRequests(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<ServiceRequestDTO>> getByEmployeeId(@PathVariable int employeeId) {
        return ResponseEntity.ok(serviceRequestService.getByEmployeeId(employeeId));
    }

    @GetMapping("/getByAsset/{assetId}")
    public ResponseEntity<List<ServiceRequestDTO>> getByAssetId(@PathVariable int assetId) {
        return ResponseEntity.ok(serviceRequestService.getByAssetId(assetId));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<ServiceRequestDTO>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) throws ResourceNotFoundException {
        logger.info("Fetching service requests by status: {}", status);
        return ResponseEntity.ok(serviceRequestService.getByStatus(status, page, size));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<ServiceRequestDTO>> getByName(@PathVariable String name) {
        logger.info("Fetching service requests by name: {}", name);
        return ResponseEntity.ok(serviceRequestService.getByName(name));
    }

    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<List<ServiceRequestDTO>> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(serviceRequestService.getByUsername(username));
    }
    @PutMapping("/updateById/{id}")
    public ResponseEntity<ServiceRequest> updateById(@PathVariable int id,
                                                     @RequestBody ServiceRequest updatedRequest) throws ResourceNotFoundException {
        logger.info("Updating service request with ID: {}", id);
        return ResponseEntity.ok(serviceRequestService.updateById(id, updatedRequest));
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Map<String, String>> deleteById(@PathVariable int id) throws ResourceNotFoundException {
        logger.info("Deleting service request with ID: {}", id);
        serviceRequestService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Service request deleted successfully with ID: " + id);
        return ResponseEntity.ok(response);
    }

}
