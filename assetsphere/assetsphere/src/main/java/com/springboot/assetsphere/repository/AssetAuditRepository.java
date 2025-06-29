package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.assetsphere.enums.AuditStatus;
import com.springboot.assetsphere.model.AssetAudit;

public interface AssetAuditRepository extends JpaRepository<AssetAudit, Integer> {

    @Query("SELECT a FROM AssetAudit a WHERE a.status = ?1")
    Page<AssetAudit> findByStatus(AuditStatus status, Pageable pageable);


    @Query("SELECT a FROM AssetAudit a WHERE a.employee.id = ?1")
    List<AssetAudit> findByEmployeeId(int employeeId);

    @Query("SELECT a FROM AssetAudit a WHERE a.asset.id = ?1")
    List<AssetAudit> findByAssetId(int assetId);

    @Query("SELECT a FROM AssetAudit a WHERE a.employee.user.username = ?1")
    List<AssetAudit> findByEmployeeUserUsername(String username);

    @Query("SELECT a FROM AssetAudit a WHERE a.employee.user.username = ?1")
    Optional<AssetAudit> findByEmployeeUsername(String username);

    @Query("SELECT COUNT(a) FROM AssetAudit a WHERE a.employee.id = ?1")
    int countByEmployeeId(int employeeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM AssetAudit a WHERE a.assetallocation.id = ?1")
    void deleteByAssetAllocationId(int allocationId);



}
