package com.springboot.assetsphere.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.dto.AssetDTO;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.service.AssetService;
import com.springboot.assetsphere.exception.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    private final Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Autowired
    private AssetService assetService;

    @PostMapping("/add-batch/{categoryId}")
    private ResponseEntity<?> addAssetBatch(@PathVariable int categoryId, @RequestBody List<Asset> assetList) throws ResourceNotFoundException {
        logger.info("Request to add {} assets under Category ID: {}", assetList.size(), categoryId);

        assetService.addAssetBatch(assetList, categoryId);

        logger.info("Assets added successfully");

        Map<String, String> map = new HashMap<>();
        map.put("message", "Assets Added Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetDTO>> getAllAsset(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching all assets with pagination - page: {}, size: {}", page, size);
        List<AssetDTO> assets = assetService.getAllAsset(page, size);
        logger.info("Returned {} assets", assets.size());
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getBYId/{id}")
    public ResponseEntity<Asset> getById(@PathVariable int id) throws ResourceNotFoundException {
        logger.info("Fetching asset by ID: {}", id);
        Asset asset = assetService.getAssetById(id);
        logger.info("Returned asset: {}", asset.getAssetName());
        return ResponseEntity.ok(asset);
    }

    @GetMapping("/getByName/{assetName}")
    public ResponseEntity<List<AssetDTO>> getByAssetName(
            @PathVariable String assetName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching assets by name: {}, page: {}, size: {}", assetName, page, size);
        List<AssetDTO> assets = assetService.getByAssetName(assetName, page, size);
        logger.info("Returned {} assets with name {}", assets.size(), assetName);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getByModel/{model}")
    public ResponseEntity<List<AssetDTO>> getByModel(
            @PathVariable String model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching assets by model: {}, page: {}, size: {}", model, page, size);
        List<AssetDTO> assets = assetService.getByModel(model, page, size);
        logger.info("Returned {} assets with model {}", assets.size(), model);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getByValue/{assetValue}")
    public ResponseEntity<List<AssetDTO>> getByAssetValue(
            @PathVariable double assetValue,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching assets by value: {}, page: {}, size: {}", assetValue, page, size);
        List<AssetDTO> assets = assetService.getByAssetValue(assetValue, page, size);
        logger.info("Returned {} assets with value {}", assets.size(), assetValue);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getByExpiry/{expiryDate}")
    public ResponseEntity<List<AssetDTO>> getByExpiryDate(
            @PathVariable String expiryDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching assets by expiry date: {}, page: {}, size: {}", expiryDate, page, size);
        LocalDate date = LocalDate.parse(expiryDate);
        List<AssetDTO> assets = assetService.getByExpiryDate(date, page, size);
        logger.info("Returned {} assets with expiry date {}", assets.size(), expiryDate);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/getByCategory/{categoryName}")
    public ResponseEntity<List<AssetDTO>> getByCategoryName(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000000") int size) {
        logger.info("Fetching assets by category name: {}, page: {}, size: {}", categoryName, page, size);
        List<AssetDTO> assets = assetService.getByCategoryName(categoryName, page, size);
        logger.info("Returned {} assets with category {}", assets.size(), categoryName);
        return ResponseEntity.ok(assets);
    }
}