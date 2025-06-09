package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.AssetTracking;

@Component
public class AssetTrackingDTO {

	private String username;
	private int assetId;
	  private String action;
	    private String remarks;


	    

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




	public List<AssetTrackingDTO> convertAssetTrackingToDto(List<AssetTracking> list) {
        List<AssetTrackingDTO> dtoList = new ArrayList<>();
        list.forEach(t -> {
            AssetTrackingDTO dto = new AssetTrackingDTO();
            dto.setUsername(t.getEmployee().getUser().getUsername());
            dto.setAssetId(t.getAsset().getId());
            dto.setAction(t.getAction().toString());
            dto.setRemarks(t.getRemarks());
             
            dtoList.add(dto);
        });
        return dtoList;
    }
}
