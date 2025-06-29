package com.springboot.assetsphere.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetReturnRequestDTO;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetReturnRequest;
import com.springboot.assetsphere.service.AssetReturnRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/assetreturnrequest")
public class AssetReturnRequestController {

    Logger logger = LoggerFactory.getLogger(AssetReturnRequestController.class);

    @Autowired
    private AssetReturnRequestService assetReturnRequestService;

    @PostMapping("/add/{employeeId}/{allocationId}")
    public ResponseEntity<?> addAssetReturnRequest(@PathVariable int employeeId,
                                                   @PathVariable int allocationId,
                                                   @RequestBody AssetReturnRequest request) throws ResourceNotFoundException, EmployeeNotFoundException {
        logger.info("Request to return asset from employeeId: {}, allocationId: {}", employeeId, allocationId);
        assetReturnRequestService.addAssetReturnRequest(employeeId, allocationId, request);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset Return Request Submitted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AssetReturnRequestDTO>> getAllAssetReturnRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all asset return requests, page: {}, size: {}", page, size);
        return ResponseEntity.ok(assetReturnRequestService.getAllAssetReturnRequests(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<AssetReturnRequestDTO>> getByEmployeeId(@PathVariable int employeeId) {
        logger.info("Fetching asset return requests for employeeId: {}", employeeId);
        return ResponseEntity.ok(assetReturnRequestService.getByEmployeeId(employeeId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<AssetReturnRequestDTO>> getByUsername(@PathVariable String username) {
        logger.info("Fetching asset return requests for username: {}", username);
        return ResponseEntity.ok(assetReturnRequestService.getByUsername(username));
    }
    
    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetReturnRequestDTO>> getByStatus(@PathVariable String status) {
        logger.info("Fetching asset return requests by status: {}", status);
        return ResponseEntity.ok(assetReturnRequestService.getByStatus(status));
    }
    @PutMapping("/update/{requestId}")
    public ResponseEntity<?> updateReturnRequest(
            @PathVariable int requestId,
            @RequestBody AssetReturnRequest updatedRequest) throws ResourceNotFoundException {

        AssetReturnRequest result = assetReturnRequestService.updateById(requestId, updatedRequest);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) throws ResourceNotFoundException {
        assetReturnRequestService.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Request deleted successfully");
        return ResponseEntity.ok(response);
    }
}
