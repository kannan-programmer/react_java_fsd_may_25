package com.springboot.assetsphere.dto;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.enums.Role;
import com.springboot.assetsphere.model.Employee;
import com.springboot.assetsphere.model.Hr;
import com.springboot.assetsphere.model.ITSupport;
import com.springboot.assetsphere.model.User;

@Component
public class UserDTO {

    private String username;  // Treated as email
    private String role;
    private String status;
    private String name;       // From Employee, Hr, or ITSupport
    private String imageUrl;   // From Employee, Hr, or ITSupport

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Conversion from User + role-specific object
    public UserDTO fromUserAndEntity(User user, Object roleEntity) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().toString());
        dto.setStatus(user.getStatus().toString());

        if (user.getRole() == Role.EMPLOYEE && roleEntity instanceof Employee emp) {
            dto.setName(emp.getName());
            dto.setImageUrl(emp.getImageUrl());
        } else if (user.getRole() == Role.HR && roleEntity instanceof Hr hr) {
            dto.setName(hr.getName());
            dto.setImageUrl(hr.getImageUrl());
        } else if (user.getRole() == Role.IT_SUPPORT && roleEntity instanceof ITSupport it) {
            dto.setName(it.getName());
            dto.setImageUrl(it.getImageUrl());
        } else {
            dto.setName("N/A");
            dto.setImageUrl(null);
        }

        return dto;
    }
}
