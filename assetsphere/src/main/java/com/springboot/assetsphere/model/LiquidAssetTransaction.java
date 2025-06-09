// LiquidAssetTransaction.java
package com.springboot.assetsphere.model;

import java.time.LocalDate;

import com.springboot.assetsphere.enums.PaymentStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "liquid_asset_transactions")
public class LiquidAssetTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double amountPaid;
    private LocalDate paidOn;
    private String paymentMethod;
    private String referenceNumber;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String financeComments;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private LiquidAssetRequest liquidAssetRequest;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LiquidAssetRequest getLiquidAssetRequest() {
        return liquidAssetRequest;
    }

    public void setLiquidAssetRequest(LiquidAssetRequest liquidAssetRequest) {
        this.liquidAssetRequest = liquidAssetRequest;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(LocalDate paidOn) {
        this.paidOn = paidOn;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

	public String getFinanceComments() {
		return financeComments;
	}

	public void setFinanceComments(String financeComments) {
		this.financeComments = financeComments;
	}
    
}
