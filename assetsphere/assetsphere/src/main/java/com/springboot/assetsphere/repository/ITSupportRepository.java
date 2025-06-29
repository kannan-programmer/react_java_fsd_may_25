package com.springboot.assetsphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.model.ITSupport;

public interface ITSupportRepository extends JpaRepository<ITSupport, Integer> {
	
	 @Query("SELECT i FROM ITSupport i WHERE i.user.username = ?1")
	    Optional<ITSupport> findByUserUsername(String username);
	}
