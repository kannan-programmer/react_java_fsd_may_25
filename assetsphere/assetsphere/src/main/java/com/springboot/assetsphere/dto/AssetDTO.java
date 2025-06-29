package com.springboot.assetsphere.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.enums.AssetStatus;
import com.springboot.assetsphere.model.Asset;

@Component
public class AssetDTO {
	private String categoryName;
	private int assetId;
	private String assetNo;
    private String assetName;
    private String model;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private double assetValue;
    private AssetStatus status;
    private String imageUrl;
     
    
    
    public String getCategoryName() {
		return categoryName;
	}



	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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



	public double getAssetValue() {
		return assetValue;
	}



	public void setAssetValue(double assetValue) {
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
	 public int getAssetId() {
			return assetId;
		}



		public void setAssetId(int assetId) {
			this.assetId = assetId;
		}





	public List<AssetDTO> convertAssetToDto(List<Asset> list) {
        List<AssetDTO> dtoList = new ArrayList<>();
        list.forEach(a -> {
            AssetDTO dto = new AssetDTO();
            dto.setCategoryName(a.getCategory().getName());
            dto.setAssetId(a.getId());
            dto.setAssetNo(a.getAssetNo());
            dto.setAssetName(a.getAssetName());
            dto.setModel(a.getModel());
            dto.setManufacturingDate(a.getManufacturingDate());
            dto.setExpiryDate(a.getExpiryDate());
            dto.setAssetValue(a.getAssetValue());
            dto.setStatus(a.getStatus());
            dto.setImageUrl(a.getImageUrl());
            dtoList.add(dto);
        });
        return dtoList;
    }
}