package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetRequest;

@Component
public class AssetRequestDTO {

	private String username;
	private int assetId;
	private String description;
    private String status;
    private String adminComments;
    
   


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




	public List<AssetRequestDTO> convertAssetRequestToDto(List<AssetRequest> list) {
        List<AssetRequestDTO> dtoList = new ArrayList<>();
        list.forEach(req -> {
            AssetRequestDTO dto = new AssetRequestDTO();
            dto.setUsername(req.getEmployee().getUser().getUsername());
            dto.setAssetId(req.getAsset().getId());
            dto.setDescription(req.getDescription());
            dto.setStatus(req.getStatus().toString());
            dto.setAdminComments(req.getAdminComments());
          
            dtoList.add(dto);
        });
        return dtoList;
    }

}
