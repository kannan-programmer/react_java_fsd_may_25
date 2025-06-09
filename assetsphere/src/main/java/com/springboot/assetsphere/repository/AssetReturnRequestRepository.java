package com.springboot.assetsphere.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.model.AssetReturnRequest;

public interface AssetReturnRequestRepository extends JpaRepository<AssetReturnRequest, Integer> {

    @Query("SELECT r FROM AssetReturnRequest r WHERE r.employee.id = ?1")
    Page<AssetReturnRequest> findByEmployeeId(int employeeId, Pageable pageable);

    @Query("SELECT r FROM AssetReturnRequest r WHERE r.status = ?1")
    Page<AssetReturnRequest> findByStatus(RequestStatus status, Pageable pageable);
}