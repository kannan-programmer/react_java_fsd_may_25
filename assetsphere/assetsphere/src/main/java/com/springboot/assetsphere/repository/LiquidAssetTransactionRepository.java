package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.PaymentStatus;
import com.springboot.assetsphere.model.LiquidAssetTransaction;

public interface LiquidAssetTransactionRepository extends JpaRepository<LiquidAssetTransaction, Integer> {


    @Query("SELECT t FROM LiquidAssetTransaction t WHERE t.employee.id = ?1")
    List<LiquidAssetTransaction> findByEmployeeId(int employeeId);

    @Query("SELECT t FROM LiquidAssetTransaction t WHERE t.status = ?1")
    Page<LiquidAssetTransaction> findByStatus(PaymentStatus status, Pageable pageable);
    
    @Query("SELECT t FROM LiquidAssetTransaction t WHERE t.employee.user.username = ?1")
    Optional<LiquidAssetTransaction> findByEmployeeUserUsername(String username);

}
