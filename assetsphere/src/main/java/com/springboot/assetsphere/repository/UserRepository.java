package com.springboot.assetsphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User getByUsername(String username);
}
