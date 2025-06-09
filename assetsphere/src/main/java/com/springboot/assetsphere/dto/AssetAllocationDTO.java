package com.springboot.assetsphere.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetAllocation;

@Component
public class AssetAllocationDTO {

	private String username;
	private int assetId;
	private LocalDate allocatedAt;
    private LocalDate returnedAt;
    private String status;
    




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





	public List<AssetAllocationDTO> convertAssetAllocationToDto(List<AssetAllocation> list) {
        List<AssetAllocationDTO> dtoList = new ArrayList<>();
        list.forEach(allocation -> {
            AssetAllocationDTO dto = new AssetAllocationDTO();
            dto.setUsername(allocation.getEmployee().getUser().getUsername());
            dto.setAssetId(allocation.getAsset().getId());
            dto.setAllocatedAt(allocation.getAllocatedAt());
            dto.setReturnedAt(allocation.getReturnedAt());
            dto.setStatus(allocation.getStatus().toString());
            dtoList.add(dto);
        });
        return dtoList;
    }
}
