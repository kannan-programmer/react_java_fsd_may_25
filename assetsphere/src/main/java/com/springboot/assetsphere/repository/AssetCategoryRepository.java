package com.springboot.assetsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.assetsphere.model.AssetCategory;

public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Integer> {

	@Query("SELECT c FROM AssetCategory c WHERE c.name = ?1")
	List<AssetCategory> findByName(String name);
	
	@Query("SELECT c FROM AssetCategory c WHERE c.name = ?1")
	Optional<AssetCategory> findByCName(String name);


}
