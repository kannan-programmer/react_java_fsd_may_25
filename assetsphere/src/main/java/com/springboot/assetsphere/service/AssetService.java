package com.springboot.assetsphere.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.assetsphere.dto.AssetDTO;
import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.Asset;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.repository.AssetCategoryRepository;
import com.springboot.assetsphere.repository.AssetRepository;

@Service
public class AssetService {

    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);

    private final AssetRepository assetRepo;
    private final AssetCategoryRepository assetcategoryRepo;
    private final AssetDTO assetDto;

    public AssetService(AssetRepository assetRepo, AssetCategoryRepository assetcategoryRepo, AssetDTO assetDto) {
        this.assetRepo = assetRepo;
        this.assetcategoryRepo = assetcategoryRepo;
        this.assetDto = assetDto;
    }

    
    @Transactional
    public void addAssetBatch(List<Asset> assetList, int categoryId) throws ResourceNotFoundException {
        AssetCategory assetCategory = assetcategoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found, Id Given is Invalid!"));

        if (assetList.isEmpty()) {
            throw new ResourceNotFoundException("Asset list is empty");
        }

        assetList.parallelStream().forEach(asset -> {
            asset.setCategory(assetCategory);
            asset.setCreatedAt(LocalDate.now());
        });

        assetRepo.saveAll(assetList);
    }

    public List<AssetDTO> getAllAsset(int page, int size) {
        logger.info("Fetching all assets - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetDto.convertAssetToDto(assetRepo.findAll(pageable).getContent());
    }

    public List<AssetDTO> getByAssetName(String assetName, int page, int size) {
        logger.info("Fetching assets by name: {}, page: {}, size: {}", assetName, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetDto.convertAssetToDto(assetRepo.findByAssetName(assetName, pageable).getContent());
    }

    public List<AssetDTO> getByModel(String model, int page, int size) {
        logger.info("Fetching assets by model: {}, page: {}, size: {}", model, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetDto.convertAssetToDto(assetRepo.findByModel(model, pageable).getContent());
    }

    public List<AssetDTO> getByAssetValue(double assetValue, int page, int size) {
        logger.info("Fetching assets by value: {}, page: {}, size: {}", assetValue, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetDto.convertAssetToDto(assetRepo.findByAssetValue(assetValue, pageable).getContent());
    }

    public List<AssetDTO> getByExpiryDate(LocalDate expiryDate, int page, int size) {
        logger.info("Fetching assets by expiry date: {}, page: {}, size: {}", expiryDate, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetDto.convertAssetToDto(assetRepo.findByExpiryDate(expiryDate, pageable).getContent());
    }

    public List<AssetDTO> getByCategoryName(String categoryName, int page, int size) {
        logger.info("Fetching assets by category name: {}, page: {}, size: {}", categoryName, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return assetDto.convertAssetToDto(assetRepo.findByCategoryName(categoryName, pageable).getContent());
    }

    public Asset getAssetById(int id) throws ResourceNotFoundException {
        logger.info("Fetching asset by id: {}", id);
        return assetRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
    }
}