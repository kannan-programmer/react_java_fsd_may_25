package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetRequestDTO;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
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
@CrossOrigin(origins = "http://localhost:5173")
public class AssetRequestController {

    Logger logger = LoggerFactory.getLogger(AssetRequestController.class);

    @Autowired
    private AssetRequestService assetRequestService;

    @PostMapping("/add/{employeeId}/{assetId}")
    public ResponseEntity<?> addAssetRequest(@PathVariable int employeeId,
                                             @PathVariable int assetId,
                                             @RequestBody AssetRequest assetRequest) throws ResourceNotFoundException, EmployeeNotFoundException, AssetNotFoundException {
        logger.info("Add request called with employeeId: {}, assetId: {}", employeeId, assetId);
        assetRequestService.addAssetRequest(employeeId, assetId, assetRequest);
        Map<String, String> map = new HashMap<>();
        map.put("message", "New Asset Requested Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetRequestDTO>> getAllAssetRequest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Get all requests called with page: {}, size: {}", page, size);
        return ResponseEntity.ok(assetRequestService.getAllAssetRequest(page, size));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AssetRequestDTO>> getByEmployeeId(@PathVariable int employeeId) {
        logger.info("Get by employeeId called with id: {}", employeeId);
        return ResponseEntity.ok(assetRequestService.getByEmployeeId(employeeId));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<AssetRequestDTO>> getByAssetId(@PathVariable int assetId) {
        logger.info("Get by assetId called with id: {}", assetId);
        return ResponseEntity.ok(assetRequestService.getByAssetId(assetId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AssetRequestDTO>> getByStatus(@PathVariable String status) {
        logger.info("Get by status called with status: {}", status);
        return ResponseEntity.ok(assetRequestService.getByStatus(status));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<AssetRequestDTO>> getByUsername(@PathVariable String username) {
        logger.info("Get by username called with username: {}", username);
        return ResponseEntity.ok(assetRequestService.getByUsername(username));
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<AssetRequest> updateById(@PathVariable int id,
                                                   @RequestBody AssetRequest updatedRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(assetRequestService.updateById(id, updatedRequest));
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) throws ResourceNotFoundException {
        assetRequestService.deleteById(id);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset Request deleted successfully with ID: " + id);
        return ResponseEntity.ok(map);
    }


}
