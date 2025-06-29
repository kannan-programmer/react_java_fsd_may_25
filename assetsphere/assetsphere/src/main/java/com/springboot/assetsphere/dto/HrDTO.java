package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.enums.Gender;
import com.springboot.assetsphere.enums.JobTitle;
import com.springboot.assetsphere.model.Hr;

@Component
public class HrDTO {

    private String username;
    private String name;
    private String contactNumber;
    private String address;
    private Gender gender;
    private String imageUrl;
    private JobTitle jobTitle;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    // Conversion method
    public List<HrDTO> convertHrToDto(List<Hr> list) {
        List<HrDTO> dtoList = new ArrayList<>();
        for (Hr hr : list) {
            HrDTO dto = new HrDTO();
            dto.setUsername(hr.getUser().getUsername());
            dto.setName(hr.getName());
            dto.setContactNumber(hr.getContactNumber());
            dto.setAddress(hr.getAddress());
            dto.setGender(hr.getGender());
            dto.setImageUrl(hr.getImageUrl());
            dto.setJobTitle(hr.getJobTitle());
            dtoList.add(dto);
        }
        return dtoList;
    }
}
