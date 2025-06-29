package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetAudit;

@Component
public class AssetAuditDTO {

	private int id;
    private String username;
    private String userFullName;
    private String jobTitle;
    private String userImageUrl;

    private int assetId;
    private String assetName;
    private String assetModel;
    private String assetImageUrl;

    private String status;
    private String comments;

    // Getters and Setters
    

    public String getUsername() {
        return username;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
        this.username = username;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

	// Conversion
	public List<AssetAuditDTO> convertAssetAuditToDto(List<AssetAudit> list) {
	    List<AssetAuditDTO> dtoList = new ArrayList<>();
	    for (AssetAudit audit : list) {
	        // skip invalid/incomplete audits
	        if (audit.getEmployee() == null || audit.getAssetallocation() == null || audit.getAsset() == null) {
	            continue;
	        }

	        AssetAuditDTO dto = new AssetAuditDTO();
	        dto.setId(audit.getId());
	        dto.setUsername(audit.getEmployee().getUser().getUsername());
	        dto.setUserFullName(audit.getEmployee().getName());
	        dto.setJobTitle(audit.getEmployee().getJobTitle());
	        dto.setUserImageUrl(audit.getEmployee().getImageUrl());

	        dto.setAssetId(audit.getAssetallocation().getId());
	        dto.setAssetName(audit.getAsset().getAssetName());
	        dto.setAssetModel(audit.getAsset().getModel());
	        dto.setAssetImageUrl(audit.getAsset().getImageUrl());

	        dto.setStatus(audit.getStatus() != null ? audit.getStatus().toString() : "PENDING");
	        dto.setComments(audit.getComments());

	        dtoList.add(dto);
	    }
	    return dtoList;
	}

}