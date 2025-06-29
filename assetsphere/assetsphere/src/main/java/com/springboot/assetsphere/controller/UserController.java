package com.springboot.assetsphere.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


@CrossOrigin(origins = "http://localhost:5173")
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
		return userService.signUp(user);
	}

	@GetMapping("/all")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/getBy-username/{username}")
	public User getUserByUsername(@PathVariable String username) {
		return userService.getUserByUsername(username);
	}

	@GetMapping("/getBy-Id/{id}")
	public User getUserById(@PathVariable int id) throws ResourceNotFoundException {
		return userService.getUserById(id);
	}

	@GetMapping("/token")
	public ResponseEntity<?> getToken(Principal principal) {
		try {
		String token =jwtUtil.createToken(principal.getName()); 
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		return ResponseEntity.status(HttpStatus.OK).body(map);
		}
		catch(Exception e){
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