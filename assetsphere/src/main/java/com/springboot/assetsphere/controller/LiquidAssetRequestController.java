package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.LiquidAssetRequestDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.LiquidAssetRequest;
import com.springboot.assetsphere.service.LiquidAssetRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liquidassetrequest")
@CrossOrigin(origins = "http://localhost:5173")
public class LiquidAssetRequestController {

    Logger logger = LoggerFactory.getLogger(LiquidAssetRequestController.class);

    @Autowired
    private LiquidAssetRequestService liquidAssetRequestService;

    @PostMapping("/add/{employeeId}")
    public ResponseEntity<?> submitLiquidAssetRequest(@PathVariable int employeeId,
                                                      @RequestBody LiquidAssetRequest request) throws ResourceNotFoundException {
        logger.info("Submitting liquid asset request for employee ID: {}", employeeId);
        liquidAssetRequestService.addLiquidAssetRequest(employeeId, request);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Liquid Asset Request Submitted Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LiquidAssetRequestDTO>> getAllRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all liquid asset requests - page: {}, size: {}", page, size);
        return ResponseEntity.ok(liquidAssetRequestService.getAllLiquidAssetRequests(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<LiquidAssetRequestDTO>> getByEmployeeId(
            @PathVariable int employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching liquid asset requests for employee ID: {}", employeeId);
        return ResponseEntity.ok(liquidAssetRequestService.getByEmployeeId(employeeId, page, size));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<LiquidAssetRequestDTO>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) throws ResourceNotFoundException {
        logger.info("Fetching liquid asset requests by status: {}", status);
        return ResponseEntity.ok(liquidAssetRequestService.getByStatus(status, page, size));
    }

    @GetMapping("/getName/{name}")
    public ResponseEntity<List<LiquidAssetRequestDTO>> getByName(@PathVariable String name) {
        logger.info("Fetching liquid asset requests by name/email: {}", name);
        return ResponseEntity.ok(liquidAssetRequestService.getByUserEmail(name));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<LiquidAssetRequestDTO>> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(liquidAssetRequestService.getByUsername(username));
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<LiquidAssetRequest> updateByUsername(@PathVariable String username,
                                                                @RequestBody LiquidAssetRequest updatedRequest) throws ResourceNotFoundException {
        logger.info("Updating liquid asset request by username: {}", username);
        return ResponseEntity.ok(liquidAssetRequestService.updateRequestByUsername(username, updatedRequest));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteByUsername(@PathVariable String username) throws ResourceNotFoundException {
        logger.info("Deleting liquid asset request by username: {}", username);
        liquidAssetRequestService.deleteRequestByUsername(username);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Liquid Asset Request deleted successfully for username: " + username);
        return ResponseEntity.ok(map);
    }
}
