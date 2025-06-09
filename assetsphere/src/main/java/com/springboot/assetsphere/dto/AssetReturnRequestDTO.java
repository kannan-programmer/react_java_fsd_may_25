package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetReturnRequest;

@Component
public class AssetReturnRequestDTO {

	private String username;
	private int allocationId;
	private String reason;
    private String status;
    private String adminComments;
   

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public int getAllocationId() {
		return allocationId;
	}


	public void setAllocationId(int allocationId) {
		this.allocationId = allocationId;
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


	public List<AssetReturnRequestDTO> convertAssetReturnToDto(List<AssetReturnRequest> list) {
        List<AssetReturnRequestDTO> dtoList = new ArrayList<>();
        list.forEach(r -> {
            AssetReturnRequestDTO dto = new AssetReturnRequestDTO();
            dto.setUsername(r.getEmployee().getUser().getUsername());
            dto.setAllocationId(r.getAllocation().getId());
            dto.setReason(r.getReason());
            dto.setStatus(r.getStatus().toString());
            dto.setAdminComments(r.getAdminComments());
            
            dtoList.add(dto);
        });
        return dtoList;
    }
}
