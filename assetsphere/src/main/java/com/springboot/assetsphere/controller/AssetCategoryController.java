package com.springboot.assetsphere.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.service.AssetCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assetcategory")
public class AssetCategoryController {

    private final Logger logger = LoggerFactory.getLogger(AssetCategoryController.class);

    @Autowired
    private AssetCategoryService assetCategoryService;

    @PostMapping("/add")
    private ResponseEntity<?> addCategory(@RequestBody AssetCategory assetCategory){
        logger.info("Adding asset category: " + assetCategory.getName());
        assetCategoryService.addCategory(assetCategory);
        logger.info("Asset category added");
        Map<String, String> map = new HashMap<>();
        map.put("message", "Category Added Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AssetCategory>> getAllCategory() {
        logger.info("Fetching all asset categories");
        return ResponseEntity.ok(assetCategoryService.getAllCategory());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AssetCategory> getCategoryById(@PathVariable int id) throws ResourceNotFoundException {
        logger.info("Fetching category by ID: " + id);
        return ResponseEntity.ok(assetCategoryService.getCategoryById(id));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<AssetCategory>> getCategoryByName(@PathVariable String name) {
        logger.info("Fetching categories by name: " + name);
        return ResponseEntity.ok(assetCategoryService.getCategoryByName(name));
    }
}