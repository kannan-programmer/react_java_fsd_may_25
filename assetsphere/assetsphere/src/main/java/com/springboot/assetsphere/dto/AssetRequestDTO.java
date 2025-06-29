package com.springboot.assetsphere.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetRequest;

@Component
public class AssetRequestDTO {

	private int id;
    private String username;
    private String userFullName;
    private String jobTitle;
    private String userImageUrl;

    private int assetId;
    private String assetName;
    private String assetModel;
    private String assetImageUrl;

    private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;
    private String description;
    private String status;
    private String adminComments;

    
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

	public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
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

	public void setAssetId(int assetId) {
        this.assetId = assetId;
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

	public String getAssetImageUrl() {
        return assetImageUrl;
    }

    public void setAssetImageUrl(String assetImageUrl) {
        this.assetImageUrl = assetImageUrl;
    }

    public String getDescription() {
        return description;
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

    // Conversion method
    public List<AssetRequestDTO> convertAssetRequestToDto(List<AssetRequest> list) {
        List<AssetRequestDTO> dtoList = new ArrayList<>();
        for (AssetRequest req : list) {
            AssetRequestDTO dto = new AssetRequestDTO();
            dto.setUsername(req.getEmployee().getUser().getUsername());
            dto.setUserFullName(req.getEmployee().getName());
            dto.setJobTitle(req.getEmployee().getJobTitle());
            dto.setUserImageUrl(req.getEmployee().getImageUrl());

            dto.setAssetId(req.getAsset().getId());
            dto.setAssetName(req.getAsset().getAssetName());
            dto.setAssetModel(req.getAsset().getModel());
            dto.setAssetImageUrl(req.getAsset().getImageUrl());
            dto.setResolvedAt(req.getResolvedAt());
			dto.setRequestedAt(req.getRequestedAt());
			dto.setId(req.getId());

            dto.setDescription(req.getDescription());
            dto.setStatus(req.getStatus().toString());
            dto.setAdminComments(req.getAdminComments());

            dtoList.add(dto);
        }
        return dtoList;
    }
}