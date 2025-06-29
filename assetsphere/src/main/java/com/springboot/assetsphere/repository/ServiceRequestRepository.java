package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.enums.ServiceStatus;
import com.springboot.assetsphere.model.ServiceRequest;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Integer> {

    @Query("SELECT s FROM ServiceRequest s WHERE s.employee.id = ?1")
    List<ServiceRequest> findByEmployeeId(int employeeId);

    @Query("SELECT s FROM ServiceRequest s WHERE s.asset.id = ?1")
    List<ServiceRequest> findByAssetId(int assetId);

    @Query("SELECT s FROM ServiceRequest s WHERE s.status = ?1")
    Page<ServiceRequest> findByStatus(ServiceStatus status, Pageable pageable);

    @Query("SELECT s FROM ServiceRequest s WHERE s.employee.name = ?1")
    List<ServiceRequest> findByEmployeeName(String name);

    @Query("SELECT s FROM ServiceRequest s WHERE s.employee.user.username = ?1")
    List<ServiceRequest> findByEmployeeUserUsername(String username);

    @Query("SELECT r FROM ServiceRequest r WHERE r.employee.user.username = ?1")
    Optional<ServiceRequest> findByEmployeeUsername(String username);

    @Query("SELECT COUNT(sr) FROM ServiceRequest sr")
    int countAll();
    
    @Query("SELECT COUNT(s) FROM ServiceRequest s WHERE s.employee.id = ?1")
    int countByEmployeeId(int employeeId);

    @Query("SELECT COUNT(s) FROM ServiceRequest s WHERE s.employee.id = ?1 AND s.status = ?2")
    int countByEmployeeIdAndStatus(int employeeId, ServiceStatus status);
}
