package com.springboot.assetsphere.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetAllocationDTO;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetAllocation;
import com.springboot.assetsphere.service.AssetAllocationService;

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
@RequestMapping("/api/assetallocation")
public class AssetAllocationController {

    private static final Logger logger = LoggerFactory.getLogger(AssetAllocationController.class);

    @Autowired
    private AssetAllocationService assetAllocationService;

    @PostMapping("/add/{employeeId}/{assetId}")
    public ResponseEntity<?> addAllocation(@PathVariable int employeeId, @PathVariable int assetId, @RequestBody AssetAllocation assetAllocation) throws ResourceNotFoundException, EmployeeNotFoundException, AssetNotFoundException {
        assetAllocationService.addAllocation(employeeId, assetId, assetAllocation);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset Allocated Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetAllocationDTO>> getAllAllocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(assetAllocationService.getAllAllocations(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<AssetAllocationDTO>> getByEmployeeId(@PathVariable int employeeId) {
      
        return ResponseEntity.ok(assetAllocationService.getAllocationsByEmployee(employeeId));
    }

    @GetMapping("/getByAssetId/{assetId}")
    public ResponseEntity<List<AssetAllocationDTO>> getByAssetId(@PathVariable int assetId) {
       
        return ResponseEntity.ok(assetAllocationService.getAllocationsByAsset(assetId));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetAllocationDTO>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(assetAllocationService.getAllocationsByStatus(status, page, size));
    }

    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<List<AssetAllocationDTO>> getByUsername(@PathVariable String username) {
  
        return ResponseEntity.ok(assetAllocationService.getAllocationsByUsername(username));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAllocation(@PathVariable int id, @RequestBody AssetAllocation updatedAllocation) throws ResourceNotFoundException {
        assetAllocationService.updateAllocation(id, updatedAllocation);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Asset Allocation updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAllocation(@PathVariable int id) throws ResourceNotFoundException {
        assetAllocationService.deleteAllocation(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Asset Allocation deleted successfully");
        return ResponseEntity.ok(response);
    }

}
