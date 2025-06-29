package com.springboot.assetsphere.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetDTO;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.Hr;
import com.springboot.assetsphere.service.AssetService;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @PostMapping("/add-batch/{categoryId}")
    public ResponseEntity<?> addAssetBatch(@PathVariable int categoryId, @RequestBody List<Asset> assetList) throws ResourceNotFoundException, AssetNotFoundException {
        assetService.addAssetBatch(assetList, categoryId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Assets Added Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetDTO>> getAllAsset(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<AssetDTO> assets = assetService.getAllAsset(page, size);
        return ResponseEntity.ok(assets);
    }

    
    @GetMapping("/getBYId/{id}")
    public ResponseEntity<Asset> getById(@PathVariable int id) throws ResourceNotFoundException, AssetNotFoundException {
        Asset asset = assetService.getAssetById(id);
        return ResponseEntity.ok(asset);
    }

    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<List<AssetDTO>> getByStatus(@PathVariable String status) {
        List<AssetDTO> assets = assetService.getByStatus(status);
        return ResponseEntity.ok(assets);
    }
    
    
    @GetMapping("/getByName/{assetName}")
    public ResponseEntity<List<AssetDTO>> getByAssetName(@PathVariable String assetName) {
        List<AssetDTO> assets = assetService.getByAssetName(assetName);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getByModel/{model}")
    public ResponseEntity<List<AssetDTO>> getByModel(@PathVariable String model) {
        List<AssetDTO> assets = assetService.getByModel(model);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getByValue/{assetValue}")
    public ResponseEntity<List<AssetDTO>> getByAssetValue(@PathVariable double assetValue) {
        List<AssetDTO> assets = assetService.getByAssetValue(assetValue);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getByExpiry/{expiryDate}")
    public ResponseEntity<List<AssetDTO>> getByExpiryDate(@PathVariable String expiryDate) {
        LocalDate date = LocalDate.parse(expiryDate);
        List<AssetDTO> assets = assetService.getByExpiryDate(date);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getByCategory/{categoryName}")
    public ResponseEntity<List<AssetDTO>> getByCategoryName(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<AssetDTO> assets = assetService.getByCategoryName(categoryName, page, size);
        return ResponseEntity.ok(assets);
    }
    
    @PostMapping("/add-with-image/{categoryId}")
    public ResponseEntity<?> addAssetWithImage(
            @PathVariable int categoryId,
            @RequestPart("asset") Asset asset,
            @RequestPart("file") MultipartFile file
    ) throws IOException, ResourceNotFoundException {

        Asset savedAsset = assetService.addAssetWithImage(asset, categoryId, file);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Asset with image added successfully");
        map.put("assetId", savedAsset.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable int id, @RequestBody Asset updatedAsset) throws ResourceNotFoundException, AssetNotFoundException {
        Asset asset = assetService.updateAsset(id, updatedAsset);
        return ResponseEntity.ok(asset);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteAsset(@PathVariable int id) throws ResourceNotFoundException, AssetNotFoundException {
        assetService.deleteAsset(id);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Asset deleted successfully");
        return ResponseEntity.ok(map);
    }
}
