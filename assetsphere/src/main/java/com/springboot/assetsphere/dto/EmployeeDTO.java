package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.enums.Gender;
import com.springboot.assetsphere.model.Employee;

@Component
public class EmployeeDTO {

	private String username;
	private String jobTitle;
	private String contactNumber;
    private String address;
    private Gender gender;
    private String imageUrl;
   


	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getJobTitle() {
		return jobTitle;
	}



	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}



	public String getContactNumber() {
		return contactNumber;
	}



	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public Gender getGender() {
		return gender;
	}



	public void setGender(Gender gender) {
		this.gender = gender;
	}



	public String getImageUrl() {
		return imageUrl;
	}



	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public List<EmployeeDTO> convertEmployeeToDto(List<Employee> list) {
        List<EmployeeDTO> dtoList = new ArrayList<>();
        list.forEach(e -> {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setUsername(e.getUser().getUsername());
            dto.setJobTitle(e.getJobTitle().toString());
            dto.setContactNumber(e.getContactNumber());
            dto.setAddress(e.getAddress());
            dto.setGender(e.getGender());
            dto.setImageUrl(e.getImageUrl());
           
            dtoList.add(dto);
        });
        return dtoList;
    }
}
