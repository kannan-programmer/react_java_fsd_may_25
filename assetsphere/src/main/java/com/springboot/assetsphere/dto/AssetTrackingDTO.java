package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetTracking;

@Component
public class AssetTrackingDTO {

    private String username;
    private String userFullName;
    private String jobTitle;

    private String userImageUrl;

    private int assetId;
    private String assetName;

    private String assetModel;
    private String assetImageUrl;

    private String action;
    private String remarks;

    // Getters and Setters

    public String getUsername() {
        return username;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    public List<AssetTrackingDTO> convertAssetTrackingToDto(List<AssetTracking> list) {
        List<AssetTrackingDTO> dtoList = new ArrayList<>();
        list.forEach(t -> {
            AssetTrackingDTO dto = new AssetTrackingDTO();

            dto.setUsername(t.getEmployee().getUser().getUsername());
            dto.setUserFullName(t.getEmployee().getName());
            dto.setJobTitle(t.getEmployee().getJobTitle());
            dto.setUserImageUrl(t.getEmployee().getImageUrl());

            dto.setAssetId(t.getAsset().getId());
            dto.setAssetModel(t.getAsset().getModel());
            dto.setAssetName(t.getAsset().getAssetName());
            dto.setAssetImageUrl(t.getAsset().getImageUrl());

            dto.setAction(t.getAction().toString());
            dto.setRemarks(t.getRemarks());

            dtoList.add(dto);
        });
        return dtoList;
    }
}