package com.springboot.assetsphere.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.LiquidAssetRequest;

@Component
public class LiquidAssetRequestDTO {

	private String username;
	private String email;
	private String itemName;
    private String itemCategory;
    private double purchaseAmount;
    private LocalDate purchaseDate;
    private String documentProofUrl;
    private String status;
    private String adminComments;
    
  

	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getItemName() {
		return itemName;
	}



	public void setItemName(String itemName) {
		this.itemName = itemName;
	}



	public String getItemCategory() {
		return itemCategory;
	}



	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}



	public double getPurchaseAmount() {
		return purchaseAmount;
	}



	public void setPurchaseAmount(double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}



	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}



	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}



	public String getDocumentProofUrl() {
		return documentProofUrl;
	}



	public void setDocumentProofUrl(String documentProofUrl) {
		this.documentProofUrl = documentProofUrl;
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



	public List<LiquidAssetRequestDTO> convertLiquidAssetRequestToDto(List<LiquidAssetRequest> list) {
        List<LiquidAssetRequestDTO> dtoList = new ArrayList<>();
        list.forEach(r -> {
            LiquidAssetRequestDTO dto = new LiquidAssetRequestDTO();
            dto.setUsername(r.getEmployee().getUser().getUsername());
            dto.setEmail(r.getEmployee().getUser().getEmail());
            dto.setItemName(r.getItemName());
            dto.setItemCategory(r.getItemCategory());
            dto.setPurchaseAmount(r.getPurchaseAmount());
            dto.setPurchaseDate(r.getPurchaseDate());
            dto.setDocumentProofUrl(r.getDocumentProofUrl());
            dto.setStatus(r.getStatus().toString());
            dto.setAdminComments(r.getAdminComments());
            dtoList.add(dto);
        });
        return dtoList;
    }

}
