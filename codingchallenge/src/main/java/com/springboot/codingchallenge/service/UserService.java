package com.springboot.codingchallenge.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.codingchallenge.model.User;
import com.springboot.codingchallenge.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	


	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}




	public User signUp(User user) {
		// encrypt the pain text password given
		String plainPassword = user.getPassword(); // <- this gives you plain password
		String encodedPassword = passwordEncoder.encode(plainPassword);
		user.setPassword(encodedPassword); // <- Now, User has encoded password

		// Save User in DB
		return userRepository.save(user);
	}




	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.getByUsername(username);
	}


}