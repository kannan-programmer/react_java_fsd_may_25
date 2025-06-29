package com.springboot.assetsphere.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.model.Asset;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
    
    List<Asset> findByAssetName(String assetName);

    @Query("SELECT a FROM Asset a WHERE a.model = ?1")
    List<Asset> findByModel(String model);

    List<Asset> findByAssetValue(double assetValue);

    List<Asset> findByExpiryDate(LocalDate expiryDate);

    @Query("SELECT a FROM Asset a WHERE a.category.name = ?1")
    Page<Asset> findByCategoryName(String categoryName, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Asset a WHERE a.status = 'AVAILABLE'")
    int countAvailableAssets();
    
    @Query("SELECT a FROM Asset a WHERE a.status = ?1")
    List<Asset> findByStatus(AssetStatus status);
}
