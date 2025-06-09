package com.springboot.assetsphere.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.ServiceStatus;
import com.springboot.assetsphere.model.ServiceRequest;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Integer> {

    @Query("SELECT s FROM ServiceRequest s WHERE s.employee.id = ?1")
    Page<ServiceRequest> findByEmployeeId(int employeeId, Pageable pageable);

    @Query("SELECT s FROM ServiceRequest s WHERE s.asset.id = ?1")
    Page<ServiceRequest> findByAssetId(int assetId, Pageable pageable);

    @Query("SELECT s FROM ServiceRequest s WHERE s.status = ?1")
    Page<ServiceRequest> findByStatus(ServiceStatus status, Pageable pageable);

    @Query("SELECT s FROM ServiceRequest s WHERE s.employee.user.email = ?1")
    Page<ServiceRequest> findByEmployeeUserEmail(String email, Pageable pageable);

    @Query("SELECT s FROM ServiceRequest s WHERE s.employee.user.username = ?1")
    Page<ServiceRequest> findByEmployeeUserUsername(String username, Pageable pageable);
}