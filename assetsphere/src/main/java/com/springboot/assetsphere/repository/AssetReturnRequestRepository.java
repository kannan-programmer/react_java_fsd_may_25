package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.model.AssetReturnRequest;

import jakarta.transaction.Transactional;

public interface AssetReturnRequestRepository extends JpaRepository<AssetReturnRequest, Integer> {

    @Query("SELECT r FROM AssetReturnRequest r")
    List<AssetReturnRequest> findAll();

    @Query("SELECT r FROM AssetReturnRequest r WHERE r.employee.id = ?1")
    List<AssetReturnRequest> findByEmployeeId(int employeeId);

    @Query("SELECT r FROM AssetReturnRequest r WHERE r.status = ?1")
    List<AssetReturnRequest> findByStatus(RequestStatus status);

    @Query("SELECT ar FROM AssetReturnRequest ar WHERE ar.employee.user.username = ?1")
    Optional<AssetReturnRequest> findByEmployeeUserUsername(String username);
    
    
    @Query("SELECT r FROM AssetReturnRequest r WHERE r.employee.user.username = ?1")
    List<AssetReturnRequest> findByEmployeeUsername(String username);

    // Get one by username and allocationId
    @Query("SELECT r FROM AssetReturnRequest r WHERE r.id = ?1")
    Optional<AssetReturnRequest> findByRequestId(Integer id);
    
    @Query("SELECT r FROM AssetReturnRequest r WHERE r.employee.user.username = ?1 AND r.id = ?2")
    Optional<AssetReturnRequest> findByUsernameAndRequestId(String username, int requestId);

	@Modifying
@Query("DELETE FROM AssetReturnRequest r WHERE r.allocation.id = ?1")
void deleteByAllocationId(int allocationId);

}