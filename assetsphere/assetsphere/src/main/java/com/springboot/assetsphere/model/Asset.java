package com.springboot.assetsphere.model;

import java.time.LocalDate;

import com.springboot.assetsphere.enums.AssetStatus;

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
@Table(name = "assets")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(unique = true)
    private String assetNo;
    private String assetName;
    private String model;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Double assetValue;
    
    @Enumerated(EnumType.STRING)
    private AssetStatus status;
    
    @Column(name = "asset_image")
    private String imageUrl;
    
    @Column(name = "asset_created_date")
    private LocalDate createdAt;
    @ManyToOne
    private AssetCategory category;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAssetNo() {
		return assetNo;
	}
	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public LocalDate getManufacturingDate() {
		return manufacturingDate;
	}
	public void setManufacturingDate(LocalDate manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}
	public LocalDate getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Double getAssetValue() {
		return assetValue;
	}
	public void setAssetValue(Double assetValue) {
		this.assetValue = assetValue;
	}
	public AssetStatus getStatus() {
		return status;
	}
	public void setStatus(AssetStatus status) {
		this.status = status;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	public AssetCategory getCategory() {
		return category;
	}
	public void setCategory(AssetCategory category) {
		this.category = category;
	}
    
	
}
