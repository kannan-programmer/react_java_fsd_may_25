package com.springboot.assetsphere.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.AllocationStatus;
import com.springboot.assetsphere.model.AssetAllocation;

public interface AssetAllocationRepository extends JpaRepository<AssetAllocation, Integer>  {

    @Query("SELECT a FROM AssetAllocation a WHERE a.employee.id = ?1")
    List<AssetAllocation> findByEmployeeId(int employeeId);  

    @Query("SELECT a FROM AssetAllocation a WHERE a.asset.id = ?1")
    List<AssetAllocation> findByAssetId(int assetId);  // no pagination

    @Query("SELECT a FROM AssetAllocation a WHERE a.status = ?1")
    Page<AssetAllocation> findByStatus(AllocationStatus status, Pageable pageable);  // with pagination

    @Query("SELECT a FROM AssetAllocation a WHERE a.employee.user.username = ?1")
    List<AssetAllocation> findByEmployeeUserUsername(String username);  // no pagination

    @Query("SELECT COUNT(aa) FROM AssetAllocation aa")
    int countAll();
    
    @Query("SELECT COUNT(a) FROM AssetAllocation a WHERE a.employee.id = ?1")
    int countByEmployeeId(int employeeId);

    @Query("SELECT COUNT(a) FROM AssetAllocation a WHERE a.employee.id = ?1 AND a.status = ?2")
    int countByEmployeeIdAndStatus(int employeeId, AllocationStatus status);
}
