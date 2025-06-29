package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.enums.Gender;
import com.springboot.assetsphere.enums.Status;
import com.springboot.assetsphere.model.Employee;

@Component
public class EmployeeDTO {

    private String name;
    private String username; // treated as email
    private String jobTitle;
    private String contactNumber;
    private String address;
    private Gender gender;
    private String imageUrl;
    private Status status;
    private int id;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
    

    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	// Conversion Method
    public List<EmployeeDTO> convertEmployeeToDto(List<Employee> list) {
        List<EmployeeDTO> dtoList = new ArrayList<>();
        list.forEach(e -> {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setName(e.getName()); // new field added
            dto.setUsername(e.getUser().getUsername()); // username is email
            dto.setJobTitle(e.getJobTitle());
            dto.setContactNumber(e.getContactNumber());
            dto.setAddress(e.getAddress());
            dto.setStatus(e.getUser().getStatus());
            dto.setGender(e.getGender());
            dto.setImageUrl(e.getImageUrl());
            dto.setId(e.getId());
            dtoList.add(dto);
        });
        return dtoList;
    }

    // Optional: Convert single Employee object
    public EmployeeDTO convertEmployeeToDto(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setUsername(e.getUser().getUsername());
        dto.setJobTitle(e.getJobTitle());
        dto.setContactNumber(e.getContactNumber());
        dto.setAddress(e.getAddress());
        dto.setGender(e.getGender());
        dto.setImageUrl(e.getImageUrl());
        return dto;
    }
}