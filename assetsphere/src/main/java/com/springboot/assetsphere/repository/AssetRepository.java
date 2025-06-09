package com.springboot.assetsphere.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.model.Asset;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
    
    Page<Asset> findByAssetName(String assetName, Pageable pageable);

    @Query("SELECT a FROM Asset a WHERE a.model = ?1")
    Page<Asset> findByModel(String model, Pageable pageable);

    Page<Asset> findByAssetValue(double assetValue, Pageable pageable);

    Page<Asset> findByExpiryDate(LocalDate expiryDate, Pageable pageable);

    @Query("SELECT a FROM Asset a WHERE a.category.name = ?1")
    Page<Asset> findByCategoryName(String categoryName, Pageable pageable);
}