package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetAudit;

@Component
public class AssetAuditDTO {

	private String username;
	private int assetId;
	private String status;
    private String comments;
   

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


		public List<AssetAuditDTO> convertAssetAuditToDto(List<AssetAudit> list) {
	        List<AssetAuditDTO> dtoList = new ArrayList<>();
	        list.forEach(audit -> {
	            AssetAuditDTO dto = new AssetAuditDTO();
	            dto.setUsername(audit.getEmployee().getUser().getUsername());
	            dto.setAssetId(audit.getAsset().getId());
	            dto.setStatus(audit.getStatus().toString());
	            dto.setComments(audit.getComments());
	           
	            dtoList.add(dto);
	        });
	        return dtoList;
		}
}
