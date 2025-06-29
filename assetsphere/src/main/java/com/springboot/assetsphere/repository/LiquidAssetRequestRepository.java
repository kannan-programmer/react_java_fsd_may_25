package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

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

    // Now returns a list (no pagination)
    @Query("SELECT l FROM LiquidAssetRequest l WHERE l.employee.name = ?1")
    List<LiquidAssetRequest> findByEmployeeUserEmail(String name);

    // Now returns a list (no pagination)
    @Query("SELECT l FROM LiquidAssetRequest l WHERE l.employee.user.username = ?1")
    List<LiquidAssetRequest> findByEmployeeUserUsername(String username);

    @Query("SELECT r FROM LiquidAssetRequest r WHERE r.employee.user.username = ?1")
    Optional<LiquidAssetRequest> findByEmployeeUsername(String username);

    @Query("SELECT COUNT(la) FROM LiquidAssetRequest la")
    int countAll();
}
