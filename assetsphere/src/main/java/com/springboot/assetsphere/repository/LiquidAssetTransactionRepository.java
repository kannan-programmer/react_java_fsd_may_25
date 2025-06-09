package com.springboot.assetsphere.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.enums.PaymentStatus;
import com.springboot.assetsphere.model.LiquidAssetTransaction;

public interface LiquidAssetTransactionRepository extends JpaRepository<LiquidAssetTransaction, Integer> {

    @Query("SELECT t FROM LiquidAssetTransaction t WHERE t.employee.id = ?1")
    Page<LiquidAssetTransaction> findByEmployeeId(int employeeId, Pageable pageable);

    @Query("SELECT t FROM LiquidAssetTransaction t WHERE t.status = :status")
    Page<LiquidAssetTransaction> findByStatus(PaymentStatus status, Pageable pageable);
}