package com.springboot.assetsphere.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.model.AssetRequest;

public interface AssetRequestRepository extends JpaRepository<AssetRequest, Integer> {

    @Query("SELECT a FROM AssetRequest a WHERE a.employee.id = ?1")
    Page<AssetRequest> findByEmployeeId(int employeeId, Pageable pageable);

    @Query("SELECT a FROM AssetRequest a WHERE a.asset.id = ?1")
    Page<AssetRequest> findByAssetId(int assetId, Pageable pageable);

    @Query("SELECT a FROM AssetRequest a WHERE a.status = ?1")
    Page<AssetRequest> findByStatus(RequestStatus status, Pageable pageable);

    @Query("SELECT ar FROM AssetRequest ar WHERE ar.employee.user.username = ?1")
    List<AssetRequest> findByEmployeeUserUsername(String username);
}