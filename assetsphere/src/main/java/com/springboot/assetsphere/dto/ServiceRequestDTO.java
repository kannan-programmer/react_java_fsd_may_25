package com.springboot.assetsphere.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.ServiceRequest;

@Component
public class ServiceRequestDTO {

	private String username;
	private int assetId;
    private String assetName;

	private String issueType;
	private String description;
	private String status;
	private String adminComments;

	private int id;
	private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;
	private String userFullName; 
    private String jobTitle;
    private String assetCategory;
    

	private String userImageUrl;     // From employee.imageUrl
	private String assetImageUrl;    // From asset.imageUrl
	private String assetModel;       // From asset.model

	// Getters and Setters

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int assetId) {
		this.assetId = assetId;
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
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdminComments() {
		return adminComments;
	}

	public void setAdminComments(String adminComments) {
		this.adminComments = adminComments;
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

	public String getAssetImageUrl() {
		return assetImageUrl;
	}

	public void setAssetImageUrl(String assetImageUrl) {
		this.assetImageUrl = assetImageUrl;
	}

	public String getAssetModel() {
		return assetModel;
	}

	public void setAssetModel(String assetModel) {
		this.assetModel = assetModel;
	}
	

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	// Conversion method
	public List<ServiceRequestDTO> convertServiceRequestToDto(List<ServiceRequest> list) {
		List<ServiceRequestDTO> dtoList = new ArrayList<>();
		list.forEach(r -> {
			ServiceRequestDTO dto = new ServiceRequestDTO();
			dto.setUsername(r.getEmployee().getUser().getUsername());
			dto.setUserFullName(r.getEmployee().getName());
            dto.setJobTitle(r.getEmployee().getJobTitle());
			dto.setAssetId(r.getAsset().getId());
            dto.setAssetCategory(r.getAsset().getCategory().getName());
            dto.setAssetName(r.getAsset().getAssetName());
			dto.setIssueType(r.getIssueType());
			dto.setDescription(r.getDescription());
			dto.setStatus(r.getStatus().toString());
			dto.setAdminComments(r.getAdminComments());

			dto.setUserImageUrl(r.getEmployee().getImageUrl());
			dto.setAssetImageUrl(r.getAsset().getImageUrl());
			dto.setAssetModel(r.getAsset().getModel());
			dto.setRequestedAt(r.getRequestedAt());
			dto.setResolvedAt(r.getResolvedAt());
			dto.setRequestedAt(r.getRequestedAt());
			dto.setId(r.getId());

			dtoList.add(dto);
		});
		return dtoList;
	}
	
	
}