package com.springboot.assetsphere.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.assetsphere.dto.AssetDTO;
import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.exception.AssetNotFoundException;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.model.Hr;
import com.springboot.assetsphere.repository.AssetCategoryRepository;
import com.springboot.assetsphere.repository.AssetRepository;

@Service
public class AssetService {

    private final AssetRepository assetRepo;
    private final AssetCategoryRepository assetcategoryRepo;
    private final AssetDTO assetDto;
	private static final Logger logger = LoggerFactory.getLogger(HRService.class);


    public AssetService(AssetRepository assetRepo, AssetCategoryRepository assetcategoryRepo, AssetDTO assetDto) {
        this.assetRepo = assetRepo;
        this.assetcategoryRepo = assetcategoryRepo;
        this.assetDto = assetDto;
    }

    @Transactional
    public void addAssetBatch(List<Asset> assetList, int categoryId) throws AssetNotFoundException {
        AssetCategory assetCategory = assetcategoryRepo.findById(categoryId)
                .orElseThrow(() -> new AssetNotFoundException("Category Not Found, Id Given is Invalid!"));

        if (assetList.isEmpty()) {
            throw new AssetNotFoundException("Asset list is empty");
        }

        assetList.parallelStream().forEach(asset -> {
            asset.setCategory(assetCategory);
            asset.setCreatedAt(LocalDate.now());
        });

        assetRepo.saveAll(assetList);
    }

    public List<AssetDTO> getAllAsset(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return assetDto.convertAssetToDto(assetRepo.findAll(pageable).getContent());
    }

    public List<AssetDTO> getByAssetName(String assetName) {
        return assetDto.convertAssetToDto(assetRepo.findByAssetName(assetName));
    }

    public List<AssetDTO> getByModel(String model) {
        return assetDto.convertAssetToDto(assetRepo.findByModel(model));
    }
    
    public List<AssetDTO> getByStatus(String status) {
        AssetStatus assetStatus = AssetStatus.valueOf(status.toUpperCase());
        List<Asset> assets = assetRepo.findByStatus(assetStatus);
        return assetDto.convertAssetToDto(assets);
    }


    public List<AssetDTO> getByAssetValue(double assetValue) {
        return assetDto.convertAssetToDto(assetRepo.findByAssetValue(assetValue));
    }

    public List<AssetDTO> getByExpiryDate(LocalDate expiryDate) {
        return assetDto.convertAssetToDto(assetRepo.findByExpiryDate(expiryDate));
    }

    public List<AssetDTO> getByCategoryName(String categoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return assetDto.convertAssetToDto(assetRepo.findByCategoryName(categoryName, pageable).getContent());
    }

    public Asset getAssetById(int id) throws AssetNotFoundException {
        return assetRepo.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found"));
    }
    
    
    public Asset addAssetWithImage(Asset asset, int categoryId, MultipartFile file)
            throws IOException, ResourceNotFoundException {

        AssetCategory assetCategory = assetcategoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        asset.setCategory(assetCategory);
        asset.setCreatedAt(LocalDate.now());

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            logger.error("Invalid file name");
            throw new RuntimeException("Invalid file name");
        }

        logger.info("Original filename: " + originalFileName);

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
        List<String> allowedExtensions = List.of("jpg", "jpeg", "png", "gif", "svg","webp");

        if (!allowedExtensions.contains(extension)) {
            logger.error("Extension not approved: " + extension);
            throw new RuntimeException("File Extension " + extension + " not allowed. Allowed: " + allowedExtensions);
        }

        logger.info("Extension approved: " + extension);

        long kbs = file.getSize() / 1024;
        if (kbs > 6000) {
            logger.error("File size too large: " + kbs + " KB");
            throw new RuntimeException("Image Oversized. Max allowed size is 6000 KB. Provided: " + kbs + " KB");
        }

        logger.info("Asset image size: " + kbs + " KB");

        String newFileName = asset.getAssetNo() + "." + extension; // e.g., a14.png
        String uploadFolder = "C:\\Users\\GOPALAKANNAN_N\\asset-sphere-ui\\public\\images";
        Files.createDirectories(Path.of(uploadFolder));
        logger.info("Directory ready: " + uploadFolder);

        Path path = Paths.get(uploadFolder, newFileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File saved at: " + path);

        asset.setImageUrl(newFileName);
        return assetRepo.save(asset);
    }



    @Transactional
    public Asset updateAsset(int id, Asset updatedAsset) throws AssetNotFoundException {
        Asset asset = assetRepo.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + id));

        if (updatedAsset.getAssetName() != null) 
        	asset.setAssetName(updatedAsset.getAssetName());
        if (updatedAsset.getModel() != null) 
        	asset.setModel(updatedAsset.getModel());
        if (updatedAsset.getManufacturingDate() != null) 
        	asset.setManufacturingDate(updatedAsset.getManufacturingDate());
        if (updatedAsset.getExpiryDate() != null) 
        	asset.setExpiryDate(updatedAsset.getExpiryDate());
        if (updatedAsset.getAssetValue() != 0.0) 
        	asset.setAssetValue(updatedAsset.getAssetValue());
        if (updatedAsset.getStatus() != null) 
        	asset.setStatus(updatedAsset.getStatus());
        if (updatedAsset.getImageUrl() != null) 
        	asset.setImageUrl(updatedAsset.getImageUrl());

        return assetRepo.save(asset);
    }

    @Transactional
    public void deleteAsset(int id) throws AssetNotFoundException {
        Asset asset = assetRepo.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + id));
        assetRepo.delete(asset);
    }
}
