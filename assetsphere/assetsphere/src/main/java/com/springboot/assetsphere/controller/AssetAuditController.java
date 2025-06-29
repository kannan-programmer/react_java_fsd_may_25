package com.springboot.assetsphere.controller;

import java.util.HashMap;


import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetAuditDTO;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.EmployeeNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetAudit;
import com.springboot.assetsphere.service.AssetAuditService;

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
@RequestMapping("/api/assetaudit")
public class AssetAuditController {

    private static final Logger logger = LoggerFactory.getLogger(AssetAuditController.class);

    @Autowired
    private AssetAuditService assetAuditService;

    @PostMapping("/add/{employeeId}/{assetId}")
    public ResponseEntity<?> createAssetAudit(@PathVariable int employeeId,
                                              @PathVariable int assetId,
                                              @RequestBody AssetAudit assetAudit) throws ResourceNotFoundException, EmployeeNotFoundException, AssetNotFoundException {
        assetAuditService.createAssetAudit(employeeId, assetId, assetAudit);
        Map<String, String> map = new HashMap<>();
        map.put("message", "AssetAudit added successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetAuditDTO>> getAllAudits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        return ResponseEntity.ok(assetAuditService.getAllAudits(page, size));
    }

    @GetMapping("/getByEmployee/{employeeId}")
    public ResponseEntity<List<AssetAuditDTO>> getAuditsByEmployee(@PathVariable int employeeId) {
        return ResponseEntity.ok(assetAuditService.getAuditsByEmployee(employeeId));
    }

    @GetMapping("/getByAsset/{assetId}")
    public ResponseEntity<List<AssetAuditDTO>> getAuditsByAsset(@PathVariable int assetId) {
        return ResponseEntity.ok(assetAuditService.getAuditsByAsset(assetId));
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetAuditDTO>> getAuditsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        return ResponseEntity.ok(assetAuditService.getAuditsByStatus(status, page, size));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AssetAudit> getAuditById(@PathVariable int id) throws ResourceNotFoundException {
        return ResponseEntity.ok(assetAuditService.getAuditById(id));
    }
    
    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<List<AssetAuditDTO>> getAuditsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(assetAuditService.getAuditsByUsername(username));
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<AssetAudit> updateAuditById(@PathVariable int id, @RequestBody AssetAudit audit)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(assetAuditService.updateAuditById(id, audit));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteAuditById(@PathVariable int id)
            throws ResourceNotFoundException {
        assetAuditService.deleteAuditById(id);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Audit deleted for ID: " + id);
        return ResponseEntity.ok(map);
    }

}
