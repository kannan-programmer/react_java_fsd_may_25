package com.springboot.assetsphere.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.AuditStatus;
import com.springboot.assetsphere.model.AssetAudit;

public interface AssetAuditRepository extends JpaRepository<AssetAudit, Integer> {

    @Query("SELECT a FROM AssetAudit a WHERE a.employee.id = ?1")
    Page<AssetAudit> findByEmployeeId(int employeeId, Pageable pageable);

    @Query("SELECT a FROM AssetAudit a WHERE a.asset.id = ?1")
    Page<AssetAudit> findByAssetId(int assetId, Pageable pageable);

    @Query("SELECT a FROM AssetAudit a WHERE a.status = ?1")
    Page<AssetAudit> findByStatus(AuditStatus status, Pageable pageable);
}