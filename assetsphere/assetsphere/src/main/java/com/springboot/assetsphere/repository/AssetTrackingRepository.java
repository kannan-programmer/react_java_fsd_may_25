package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.enums.TrackingAction;
import com.springboot.assetsphere.model.AssetTracking;

public interface AssetTrackingRepository extends JpaRepository<AssetTracking, Integer> {

    @Query("SELECT t FROM AssetTracking t WHERE t.employee.id = ?1")
    List<AssetTracking> findByEmployeeId(int employeeId);

    @Query("SELECT t FROM AssetTracking t WHERE t.asset.id = ?1")
    List<AssetTracking> findByAssetId(int assetId);

    @Query("SELECT t FROM AssetTracking t WHERE t.action = ?1")
    List<AssetTracking> findByAction(TrackingAction allocated);

    @Query("SELECT t FROM AssetTracking t WHERE t.employee.user.username = ?1")
    List<AssetTracking> findByEmployeeUserUsername(String username);

   
}
