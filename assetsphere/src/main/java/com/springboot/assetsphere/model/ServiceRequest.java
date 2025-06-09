package com.springboot.assetsphere.model;

import java.time.LocalDateTime;

import com.springboot.assetsphere.enums.ServiceStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_requests")
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String issueType;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
    
    private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;
    private String adminComments;

    @ManyToOne
    private Asset asset;

    @ManyToOne
    private Employee employee;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ServiceStatus getStatus() {
		return status;
	}

	public void setStatus(ServiceStatus status) {
		this.status = status;
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

	public String getAdminComments() {
		return adminComments;
	}

	public void setAdminComments(String adminComments) {
		this.adminComments = adminComments;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
    
    
}
