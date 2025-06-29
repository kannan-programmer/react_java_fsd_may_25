package com.springboot.assetsphere.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetReturnRequest;

@Component
public class AssetReturnRequestDTO {

	
    private String username;
    private String userFullName;
    private String jobTitle;
    private String userImageUrl;

    private int id;
	private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;
    private int allocationId;
    private String assetName;
    private String assetModel;
    private String assetImageUrl;

    private String assetCategory;
    private String reason;
    private String status;
    private String adminComments;

    // Getters and Setters
    public String getUsername() {
        return username;
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

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    // Conversion logic
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

	
	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public List<AssetReturnRequestDTO> convertAssetReturnToDto(List<AssetReturnRequest> list) {
        List<AssetReturnRequestDTO> dtoList = new ArrayList<>();
        list.forEach(r -> {
            AssetReturnRequestDTO dto = new AssetReturnRequestDTO();

            dto.setUsername(r.getEmployee().getUser().getUsername());
            dto.setUserFullName(r.getEmployee().getName());
            dto.setJobTitle(r.getEmployee().getJobTitle());
            dto.setUserImageUrl(r.getEmployee().getImageUrl());
            if (r.getAllocation() != null && r.getAllocation().getAsset() != null) {
                dto.setAllocationId(r.getAllocation().getId());

                if (r.getAllocation().getAsset().getCategory() != null)
                    dto.setAssetCategory(r.getAllocation().getAsset().getCategory().getName());
                else
                    dto.setAssetCategory("N/A");

                dto.setAssetModel(r.getAllocation().getAsset().getModel());
                dto.setAssetName(r.getAllocation().getAsset().getAssetName());
                dto.setAssetImageUrl(r.getAllocation().getAsset().getImageUrl());
            } else {
                dto.setAllocationId(0);
                dto.setAssetCategory("N/A");
                dto.setAssetModel("N/A");
                dto.setAssetName("N/A");
                dto.setAssetImageUrl("N/A");
            }

            dto.setReason(r.getReason());
            dto.setStatus(r.getStatus().toString());
            dto.setAdminComments(r.getAdminComments());
            dto.setResolvedAt(r.getResolvedAt());
			dto.setRequestedAt(r.getRequestedAt());
			dto.setId(r.getId());

            dtoList.add(dto);
        });
        return dtoList;
    }
}