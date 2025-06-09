package com.springboot.assetsphere.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.assetsphere.model.User;

@Component
public class UserDTO {

    private String username;
    private String email;
    private String password;
    private String role;
    private String status;

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    // Conversion method from User entities to DTOs
    public List<UserDTO> convertUserToDto(List<User> list) {
        List<UserDTO> dtoList = new ArrayList<>();
        list.forEach(user -> {
            UserDTO dto = new UserDTO();
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPassword(user.getPassword()); // Typically, you wouldn’t expose passwords in DTOs!
            dto.setRole(user.getRole().toString());
            dto.setStatus(user.getStatus().toString());
            dtoList.add(dto);
        });
        return dtoList;
    }
}
