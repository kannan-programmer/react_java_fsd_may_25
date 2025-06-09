package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.ServiceRequest;

@Component
public class ServiceRequestDTO {

	private String username;
	private int assetId;
	private String issueType;
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



	public String getIssueType() {
		return issueType;
	}



	public void setIssueType(String issueType) {
		this.issueType = issueType;
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



	public List<ServiceRequestDTO> convertServiceRequestToDto(List<ServiceRequest> list) {
        List<ServiceRequestDTO> dtoList = new ArrayList<>();
        list.forEach(r -> {
            ServiceRequestDTO dto = new ServiceRequestDTO();
            dto.setUsername(r.getEmployee().getUser().getUsername());
            dto.setAssetId(r.getAsset().getId());
            dto.setIssueType(r.getIssueType());
            dto.setDescription(r.getDescription());
            dto.setStatus(r.getStatus().toString());
            dto.setAdminComments(r.getAdminComments());
            
            
            dtoList.add(dto);
        });
        return dtoList;
    }
}
