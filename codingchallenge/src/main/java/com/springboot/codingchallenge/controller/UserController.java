package com.springboot.codingchallenge.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.codingchallenge.exception.ResourceNotFoundException;
import com.springboot.codingchallenge.model.User;
import com.springboot.codingchallenge.service.UserService;
import com.springboot.codingchallenge.util.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User saved = userService.signUp(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Get token using username from authenticated Principal
    @GetMapping("/token")
    public ResponseEntity<?> getToken(Principal principal) {
        try {
            String token = jwtUtil.createToken(principal.getName());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user: " + e.getMessage());
        }
    }

    // Get user by email (username)
    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) throws ResourceNotFoundException {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}