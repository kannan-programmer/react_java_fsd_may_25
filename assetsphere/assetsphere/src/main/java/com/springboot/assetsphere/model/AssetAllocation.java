package com.springboot.assetsphere.model;

import java.time.LocalDate;

import com.springboot.assetsphere.enums.AllocationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "asset_allocations")
public class AssetAllocation {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate allocatedAt;
    private LocalDate returnedAt;
    
    @Enumerated(EnumType.STRING)
    private AllocationStatus status;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Asset asset;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getAllocatedAt() {
		return allocatedAt;
	}

	public void setAllocatedAt(LocalDate allocatedAt) {
		this.allocatedAt = allocatedAt;
	}

	public LocalDate getReturnedAt() {
		return returnedAt;
	}

	public void setReturnedAt(LocalDate returnedAt) {
		this.returnedAt = returnedAt;
	}

	public AllocationStatus getStatus() {
		return status;
	}

	public void setStatus(AllocationStatus status) {
		this.status = status;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}
    
    
}
