package com.springboot.assetsphere.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.model.LiquidAssetRequest;

public interface LiquidAssetRequestRepository extends JpaRepository<LiquidAssetRequest, Integer> {

    @Query("SELECT l FROM LiquidAssetRequest l WHERE l.employee.id = ?1")
    Page<LiquidAssetRequest> findByEmployeeId(int employeeId, Pageable pageable);

    @Query("SELECT l FROM LiquidAssetRequest l WHERE l.status = ?1")
    Page<LiquidAssetRequest> findByStatus(RequestStatus status, Pageable pageable);

    @Query("SELECT l FROM LiquidAssetRequest l WHERE l.employee.user.email = ?1")
    Page<LiquidAssetRequest> findByEmployeeUserEmail(String email, Pageable pageable);

    @Query("SELECT l FROM LiquidAssetRequest l WHERE l.employee.user.username = ?1")
    Page<LiquidAssetRequest> findByEmployeeUserUsername(String username, Pageable pageable);
}