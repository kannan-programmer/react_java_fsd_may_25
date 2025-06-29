package com.springboot.assetsphere.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetAllocation;

@Component
public class AssetAllocationDTO {

	private int id;
	private int employeeId;
    private String username;
    private String userFullName;
    private String jobTitle;
    private String userImageUrl;
    private int assetId;
    private String assetModel;
    private String assetName;
    private String assetImageUrl;
    private LocalDate allocatedAt;
    private LocalDate returnedAt;
    private String status;

    // Getters and Setters

    public String getUsername() {
        return username;
    }
    

    public int getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}


	public void setUsername(String username) {
        this.username = username;
    }

    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getAssetModel() {
        return assetModel;
    }

    public void setAssetModel(String assetModel) {
        this.assetModel = assetModel;
    }

    public String getAssetImageUrl() {
        return assetImageUrl;
    }

    public void setAssetImageUrl(String assetImageUrl) {
        this.assetImageUrl = assetImageUrl;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	// Conversion method
    public List<AssetAllocationDTO> convertAssetAllocationToDto(List<AssetAllocation> list) {
        List<AssetAllocationDTO> dtoList = new ArrayList<>();
        for (AssetAllocation allocation : list) {
            AssetAllocationDTO dto = new AssetAllocationDTO();
            dto.setUsername(allocation.getEmployee().getUser().getUsername());
            dto.setUserFullName(allocation.getEmployee().getName());
            dto.setJobTitle(allocation.getEmployee().getJobTitle());
            dto.setUserImageUrl(allocation.getEmployee().getImageUrl());

            dto.setAssetId(allocation.getAsset().getId());
            dto.setEmployeeId(allocation.getEmployee().getId());
            dto.setAssetModel(allocation.getAsset().getModel());
            dto.setAssetName(allocation.getAsset().getAssetName());
            dto.setAssetImageUrl(allocation.getAsset().getImageUrl());

            dto.setId(allocation.getId());
            dto.setAllocatedAt(allocation.getAllocatedAt());
            dto.setReturnedAt(allocation.getReturnedAt());
            dto.setStatus(allocation.getStatus().toString());

            dtoList.add(dto);
        }
        return dtoList;
    }
}