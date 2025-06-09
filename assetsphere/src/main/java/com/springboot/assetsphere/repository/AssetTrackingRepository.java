package com.springboot.assetsphere.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.model.AssetTracking;

public interface AssetTrackingRepository extends JpaRepository<AssetTracking, Integer> {

    @Query("SELECT t FROM AssetTracking t WHERE t.employee.id = ?1")
    Page<AssetTracking> findByEmployeeId(int employeeId, Pageable pageable);

    @Query("SELECT t FROM AssetTracking t WHERE t.asset.id = ?1")
    Page<AssetTracking> findByAssetId(int assetId, Pageable pageable);

    @Query("SELECT t FROM AssetTracking t WHERE t.action = ?1")
    Page<AssetTracking> findByAction(RequestStatus action, Pageable pageable);

    @Query("SELECT t FROM AssetTracking t WHERE t.employee.user.email = ?1")
    Page<AssetTracking> findByEmployeeUserEmail(String email, Pageable pageable);

    @Query("SELECT t FROM AssetTracking t WHERE t.employee.user.username = ?1")
    Page<AssetTracking> findByEmployeeUserUsername(String username, Pageable pageable);
}