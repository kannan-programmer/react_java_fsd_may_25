package com.springboot.assetsphere.controller;

import java.security.Principal;
import java.util.List;

import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.service.UserService;
import com.springboot.assetsphere.util.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public User signUp(@RequestBody User user) {
		logger.info("Signing up user with username: " + user.getUsername());
		return userService.signUp(user);
	}

	@GetMapping("/all")
	public List<User> getAllUsers() {
		logger.info("Fetching all users");
		return userService.getAllUsers();
	}

	@GetMapping("/getBy-username/{username}")
	public User getUserByUsername(@PathVariable String username) {
		logger.info("Fetching user by username: " + username);
		return userService.getUserByUsername(username);
	}

	@GetMapping("/getBy-Id/{id}")
	public User getUserById(@PathVariable int id) throws ResourceNotFoundException {
		logger.info("Fetching user by ID: " + id);
		return userService.getUserById(id);
	}

	@GetMapping("/token")
	public ResponseEntity<?> getToken(Principal principal) {
		try {
			logger.info("Generating token for user: " + principal.getName());
			String token = jwtUtil.createToken(principal.getName());
			return ResponseEntity.status(HttpStatus.OK).body(token);
		} catch (Exception e) {
			logger.error("Error generating token: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@GetMapping("/userInfo")
	public ResponseEntity<?> getLoggedInUserDetails(Principal principal) {
		try {
			String username = principal.getName();
			logger.info("Fetching logged-in user details for username: " + username);
			Object userDetails = userService.getUserInfo(username);
			return ResponseEntity.ok(userDetails);
		} catch (Exception e) {
			logger.error("User not found: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
		}
	}
}