package com.springboot.assetsphere.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.springboot.assetsphere.enums.RequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "liquid_asset_requests")
public class LiquidAssetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String itemName;
    private String itemCategory;
    private Double purchaseAmount;
    private LocalDate purchaseDate;
    private String documentProofUrl;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private String adminComments;
    private LocalDateTime submittedAt;
    private LocalDateTime resolvedAt;
    

    @ManyToOne
    private Employee employee;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getItemCategory() {
		return itemCategory;
	}


	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}


	public Double getPurchaseAmount() {
		return purchaseAmount;
	}


	public void setPurchaseAmount(Double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}


	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}


	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}


	public String getDocumentProofUrl() {
		return documentProofUrl;
	}


	public void setDocumentProofUrl(String documentProofUrl) {
		this.documentProofUrl = documentProofUrl;
	}


	public RequestStatus getStatus() {
		return status;
	}


	public void setStatus(RequestStatus status) {
		this.status = status;
	}


	public String getAdminComments() {
		return adminComments;
	}


	public void setAdminComments(String adminComments) {
		this.adminComments = adminComments;
	}


	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}


	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}


	public LocalDateTime getResolvedAt() {
		return resolvedAt;
	}


	public void setResolvedAt(LocalDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}


	public Employee getEmployee() {
		return employee;
	}


	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

    
    
}

