package com.springboot.assetsphere.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetTrackingDTO;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetTracking;
import com.springboot.assetsphere.service.AssetTrackingService;

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
@RequestMapping("/api/assettracking")
public class AssetTrackingController {

    @Autowired
    private AssetTrackingService assetTrackingService;

    @PostMapping("/add/{employeeId}/{assetId}")
    public ResponseEntity<?> addTrackingLog(@PathVariable int employeeId,
                                            @PathVariable int assetId,
                                            @RequestBody AssetTracking tracking) throws ResourceNotFoundException, AssetNotFoundException, EmployeeNotFoundException {
        assetTrackingService.addTrackingLog(employeeId, assetId, tracking);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset Tracking Added Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetTrackingDTO>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        return ResponseEntity.ok(assetTrackingService.getAllTrackingLogs(page, size));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AssetTrackingDTO>> getByEmployeeId(@PathVariable int employeeId) {
        return ResponseEntity.ok(assetTrackingService.getByEmployeeId(employeeId));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<AssetTrackingDTO>> getByAssetId(@PathVariable int assetId) {
        return ResponseEntity.ok(assetTrackingService.getByAssetId(assetId));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetTrackingDTO>> getByAction(@PathVariable String status) {
        return ResponseEntity.ok(assetTrackingService.getByAction(status));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<AssetTrackingDTO>> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(assetTrackingService.getByUsername(username));
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<AssetTracking> updateByUsername(@PathVariable String username,
                                                          @RequestBody AssetTracking updatedTracking) throws ResourceNotFoundException {
        return ResponseEntity.ok(assetTrackingService.updateByUsername(username, updatedTracking));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Map<String, String>> deleteByUsername(@PathVariable String username) throws ResourceNotFoundException {
        assetTrackingService.deleteByUsername(username);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset tracking entry deleted successfully for username: " + username);
        return ResponseEntity.ok(map);
    }
}
