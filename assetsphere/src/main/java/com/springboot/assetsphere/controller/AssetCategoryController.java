package com.springboot.assetsphere.controller;

import java.util.List;

import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.service.AssetCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assetcategory")
@CrossOrigin(origins = "http://localhost:5173")
public class AssetCategoryController {

    private final Logger logger = LoggerFactory.getLogger(AssetCategoryController.class);

    @Autowired
    private AssetCategoryService assetCategoryService;

    @PostMapping("/add")
    private AssetCategory addCategory(@RequestBody AssetCategory assetCategory){
        logger.info("Adding asset category: " + assetCategory.getName());
        logger.info("Asset category added");
        
        return  assetCategoryService.addCategory(assetCategory);
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
    
    @PutMapping("/update/{name}")
    public ResponseEntity<AssetCategory> updateByName(@PathVariable String name, @RequestBody AssetCategory updatedCategory) throws ResourceNotFoundException {
        logger.info("Updating category by name: {}", name);
        return ResponseEntity.ok(assetCategoryService.updateCategoryByName(name, updatedCategory));
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteByName(@PathVariable String name) throws ResourceNotFoundException {
        logger.info("Deleting category by name: {}", name);
        assetCategoryService.deleteCategoryByName(name);
        return ResponseEntity.ok("Asset category deleted successfully for name: " + name);
    }

}