package com.springboot.assetsphere.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.AssetCategory;
import com.springboot.assetsphere.repository.AssetCategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetCategoryService {

	private static final Logger logger = LoggerFactory.getLogger(AssetCategoryService.class);

	private AssetCategoryRepository assetcategoryRepo;

	public AssetCategoryService(AssetCategoryRepository assetcategoryRepo) {
		super();
		this.assetcategoryRepo = assetcategoryRepo;
	}

	public AssetCategory addCategory(AssetCategory assetCategory) {
		logger.info("Adding new asset category: {}", assetCategory.getName());
		return assetcategoryRepo.save(assetCategory);
	}

	public List<AssetCategory> getAllCategory() {
		logger.info("Fetching all asset categories");
		return assetcategoryRepo.findAll();
	}

	public AssetCategory getCategoryById(int id) throws ResourceNotFoundException {
		logger.info("Fetching asset category by ID: {}", id);
        return assetcategoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found, Id Given is Invalid!"));
    }

    public List<AssetCategory> getCategoryByName(String name) {
    	logger.info("Fetching asset categories by name: {}", name);
        return assetcategoryRepo.findByName(name);
    }
    
    @Transactional
    public AssetCategory updateCategoryByName(String name, AssetCategory updatedCategory) throws ResourceNotFoundException {
        AssetCategory existing = assetcategoryRepo.findByCName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));

        if (updatedCategory.getDescription() != null)
            existing.setDescription(updatedCategory.getDescription());

        return assetcategoryRepo.save(existing);
    }

    @Transactional
    public void deleteCategoryByName(String name) throws ResourceNotFoundException {
        AssetCategory category = assetcategoryRepo.findByCName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        assetcategoryRepo.delete(category);
    }

}