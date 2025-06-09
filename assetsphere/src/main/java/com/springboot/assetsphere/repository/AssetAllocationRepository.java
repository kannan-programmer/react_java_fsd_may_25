package com.springboot.assetsphere.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.model.AssetAllocation;

public interface AssetAllocationRepository extends JpaRepository<AssetAllocation, Integer>  {

    @Query("SELECT a FROM AssetAllocation a WHERE a.employee.id = ?1")
    Page<AssetAllocation> findByEmployeeId(int employeeId, Pageable pageable);

    @Query("SELECT a FROM AssetAllocation a WHERE a.asset.id = ?1")
    Page<AssetAllocation> findByAssetId(int assetId, Pageable pageable);

    @Query("SELECT a FROM AssetAllocation a WHERE a.status = ?1")
    Page<AssetAllocation> findByStatus(AllocationStatus status, Pageable pageable);
}