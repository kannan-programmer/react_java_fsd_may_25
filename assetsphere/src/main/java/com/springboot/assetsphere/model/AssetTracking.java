package com.springboot.assetsphere.model;

import java.time.LocalDateTime;

import com.springboot.assetsphere.enums.RequestStatus;
import com.springboot.assetsphere.enums.TrackingAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "asset_tracking_log")
public class AssetTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    
    @Enumerated(EnumType.STRING)
    private TrackingAction action;

    private String remarks;
    private LocalDateTime timestamp;
    

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

	public TrackingAction getAction() {
		return action;
	}

	public void setAction(TrackingAction action) {
		this.action = action;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
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
