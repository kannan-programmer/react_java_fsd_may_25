package com.springboot.assetsphere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.model.Hr;

public interface HrRepository extends JpaRepository<Hr, Integer> {

	 @Query("SELECT h FROM Hr h WHERE h.user.username = ?1")
	    Optional<Hr> findByUserUsername(String username);
}
