package com.springboot.codingchallenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.codingchallenge.enums.Speciality;
import com.springboot.codingchallenge.model.Doctor;

public interface DoctorRepository extends CrudRepository<Doctor, Integer> {

	@Query("SELECT d FROM Doctor d WHERE d.speciality = ?1")
	List<Doctor> findBySpecialization(Speciality speciality);

}