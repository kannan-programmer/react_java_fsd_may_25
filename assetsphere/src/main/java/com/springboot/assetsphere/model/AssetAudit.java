package com.springboot.assetsphere.model;

import java.time.LocalDateTime;

import com.springboot.assetsphere.enums.AuditStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "asset_audits")
public class AssetAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Enumerated(EnumType.STRING)
    private AuditStatus status;
    
    private String comments;
    private LocalDateTime auditedAt;
    

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

	public AuditStatus getStatus() {
		return status;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDateTime getAuditedAt() {
		return auditedAt;
	}

	public void setAuditedAt(LocalDateTime auditedAt) {
		this.auditedAt = auditedAt;
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