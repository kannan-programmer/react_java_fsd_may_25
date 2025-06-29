package com.springboot.assetsphere.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.LiquidAssetTransaction;

@Component
public class LiquidAssetTransactionDTO {

    private String username;
	private int userId;// email is stored in username
    private String name; 
    private String jobTitle;
// Employee name
    private int requestId;
    private double amountPaid;
    private LocalDate paidOn;
    private String paymentMethod;
    private String referenceNumber;
    private String status;

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

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(LocalDate paidOn) {
        this.paidOn = paidOn;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LiquidAssetTransactionDTO> convertLiquidTransactionToDto(List<LiquidAssetTransaction> list) {
        List<LiquidAssetTransactionDTO> dtoList = new ArrayList<>();
        for (LiquidAssetTransaction t : list) {
            LiquidAssetTransactionDTO dto = new LiquidAssetTransactionDTO();
            dto.setUsername(t.getEmployee().getUser().getUsername());
			dto.setUserId(t.getEmployee().getUser().getId());

            dto.setName(t.getEmployee().getName());
            dto.setJobTitle(t.getEmployee().getJobTitle());
            dto.setRequestId(t.getLiquidAssetRequest().getId());
            dto.setAmountPaid(t.getAmountPaid());
            dto.setPaidOn(t.getPaidOn());
            dto.setPaymentMethod(t.getPaymentMethod());
            dto.setReferenceNumber(t.getReferenceNumber());
            dto.setStatus(t.getStatus().toString());
            dtoList.add(dto);
        }
        return dtoList;
    }
}