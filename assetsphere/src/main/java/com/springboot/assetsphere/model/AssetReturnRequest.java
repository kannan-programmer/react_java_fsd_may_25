package com.springboot.assetsphere.model;

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
@Table(name = "asset_return_requests")
public class AssetReturnRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String reason;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String adminComments;
    private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;
    

    @ManyToOne
    private AssetAllocation allocation;

    @ManyToOne
    private Employee employee;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public LocalDateTime getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(LocalDateTime requestedAt) {
		this.requestedAt = requestedAt;
	}

	public LocalDateTime getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(LocalDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

	public AssetAllocation getAllocation() {
		return allocation;
	}

	public void setAllocation(AssetAllocation allocation) {
		this.allocation = allocation;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

    
    
}

