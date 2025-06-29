package com.springboot.assetsphere.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.LiquidAssetRequest;

@Component
public class LiquidAssetRequestDTO {

	private String username; 
	private int userId;// email is stored in username
	private String name;  
    private String jobTitle;
	private String itemName;
	private String itemCategory;
	private double purchaseAmount;
	private LocalDate purchaseDate;
	private String documentProofUrl;
	private String status;
	private String adminComments;

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	// Conversion method
	public List<LiquidAssetRequestDTO> convertLiquidAssetRequestToDto(List<LiquidAssetRequest> list) {
		List<LiquidAssetRequestDTO> dtoList = new ArrayList<>();
		for (LiquidAssetRequest r : list) {
			LiquidAssetRequestDTO dto = new LiquidAssetRequestDTO();
			dto.setUsername(r.getEmployee().getUser().getUsername());
			dto.setUserId(r.getEmployee().getUser().getId());
			dto.setName(r.getEmployee().getName());
			dto.setJobTitle(r.getEmployee().getJobTitle());
			dto.setItemName(r.getItemName());
			dto.setItemCategory(r.getItemCategory());
			dto.setPurchaseAmount(r.getPurchaseAmount());
			dto.setPurchaseDate(r.getPurchaseDate());
			dto.setDocumentProofUrl(r.getDocumentProofUrl());
			dto.setStatus(r.getStatus().toString());
			dto.setAdminComments(r.getAdminComments());
			dtoList.add(dto);
		}
		return dtoList;
	}
}