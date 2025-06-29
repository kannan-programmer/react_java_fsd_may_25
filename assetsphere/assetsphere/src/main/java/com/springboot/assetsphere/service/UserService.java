package com.springboot.assetsphere.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.assetsphere.exception.ResourceNotFoundException;
import com.springboot.assetsphere.model.User;
import com.springboot.assetsphere.repository.EmployeeRepository;
import com.springboot.assetsphere.repository.HrRepository;
import com.springboot.assetsphere.repository.ITSupportRepository;
import com.springboot.assetsphere.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private EmployeeRepository employeeRepo;
	private ITSupportRepository itSupportRepo;
	private HrRepository hrRepo;



	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepo,
			ITSupportRepository itSupportRepo, HrRepository hrRepo) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.employeeRepo = employeeRepo;
		this.itSupportRepo = itSupportRepo;
		this.hrRepo = hrRepo;
	}

	public User signUp(User user) {
		System.out.println("signUp() started");

		String plainPassword = user.getPassword(); 
		String encodedPassword =  passwordEncoder.encode(plainPassword);
		user.setPassword(encodedPassword);
		user.setCreatedAt(LocalDate.now());

		System.out.println("signUp() completed");
		return userRepository.save(user);
	}
	
	public List<User> getAllUsers() {
		System.out.println("getAllUsers() called");
		return userRepository.findAll();
	}
	 
	public User getUserByUsername(String username) {
		System.out.println("getUserByUsername() called");
		return userRepository.getByUsername(username);
	}

	public User getUserById(int id) throws ResourceNotFoundException {
		System.out.println("getUserById() called");
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
	}
	
	public Object getUserInfo(String username) {
	    System.out.println("getUserInfo() started");

	    User user = userRepository.getByUsername(username);
	    if (user == null) {
	        System.out.println("User not found");
	        throw new RuntimeException("User not found with username: " + username);
	    }

	    switch (user.getRole()) {
	        case HR:
	            return hrRepo.findByUserUsername(username)
	                    .orElseThrow(() -> {
	                        System.out.println("HR not found");
	                        return new RuntimeException("HR info not found for username: " + username);
	                    });

	        case IT_SUPPORT:
	            return itSupportRepo.findByUserUsername(username)
	                    .orElseThrow(() -> {
	                        System.out.println("IT Support not found");
	                        return new RuntimeException("IT Support info not found for username: " + username);
	                    });

	        case EMPLOYEE:
	            return employeeRepo.findByUserUsername(username)
	                    .orElseThrow(() -> {
	                        System.out.println("Employee not found");
	                        return new RuntimeException("Employee not found for username: " + username);
	                    });

	        default:
	            System.out.println("Returning raw User object");
	            return user;
	    }}

	public Optional<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}
}