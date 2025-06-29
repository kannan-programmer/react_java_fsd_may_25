package com.springboot.assetsphere.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.enums.Gender;
import com.springboot.assetsphere.enums.JobTitle;
import com.springboot.assetsphere.model.ITSupport;

@Component
public class ITSupportDTO {

    private String username;
    private String name;
    private String contactNumber;
    private String address;
    private Gender gender;
    private String imageUrl;
    private JobTitle jobTitle;
    private LocalDate createdAt;

    // Getters & Setters
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    // Converter method
    public List<ITSupportDTO> convertITSupportToDto(List<ITSupport> list) {
        List<ITSupportDTO> dtoList = new ArrayList<>();
        list.forEach(it -> {
            ITSupportDTO dto = new ITSupportDTO();
            dto.setUsername(it.getUser().getUsername());
            dto.setName(it.getName());
            dto.setContactNumber(it.getContactNumber());
            dto.setAddress(it.getAddress());
            dto.setGender(it.getGender());
            dto.setImageUrl(it.getImageUrl());
            dto.setJobTitle(it.getJobTitle());
            dto.setCreatedAt(it.getCreatedAt());
            dtoList.add(dto);
        });
        return dtoList;
    }
}
