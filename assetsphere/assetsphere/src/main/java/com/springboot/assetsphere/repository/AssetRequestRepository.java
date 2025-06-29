package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.model.AssetRequest;

public interface AssetRequestRepository extends JpaRepository<AssetRequest, Integer> {

    @Query("SELECT a FROM AssetRequest a WHERE a.employee.id = ?1")
    List<AssetRequest> findByEmployeeId(int employeeId);

    @Query("SELECT a FROM AssetRequest a WHERE a.asset.id = ?1")
    List<AssetRequest> findByAssetId(int assetId);

    @Query("SELECT a FROM AssetRequest a WHERE a.status = ?1")
    List<AssetRequest> findByStatus(RequestStatus status);

    @Query("SELECT r FROM AssetRequest r WHERE r.employee.user.username = ?1")
    List<AssetRequest> findByEmployeeUserUsername(String username);

    @Query("SELECT ar FROM AssetRequest ar WHERE ar.employee.user.username = ?1")
    Optional<AssetRequest> findByEmployeeUsername(String username);

    @Query("SELECT COUNT(ar) FROM AssetRequest ar WHERE ar.status = ?1")
    int countByStatus(RequestStatus status);
}
