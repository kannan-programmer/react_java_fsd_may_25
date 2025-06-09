package com.springboot.codingchallenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springboot.codingchallenge.model.MedicalHistory;
import com.springboot.codingchallenge.model.Patient;

public interface MedicalHistoryRepository extends CrudRepository<MedicalHistory, Integer> {

   

	@Query("select mh.patient from MedicalHistory mh where mh.patient.id = :patientId")
	List<Patient> findPatientsByPatientId(@Param("patientId") int patientId);
}