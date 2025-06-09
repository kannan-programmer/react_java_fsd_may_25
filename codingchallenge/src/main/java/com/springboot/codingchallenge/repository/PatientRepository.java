package com.springboot.codingchallenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.codingchallenge.enums.Speciality;
import com.springboot.codingchallenge.model.Patient;

public interface PatientRepository extends CrudRepository<Patient, Integer> {

    @Query("SELECT p FROM Patient p WHERE p.doctor.id = ?1")
    List<Patient> findByDoctorId(int doctorId);

    @Query("select p from Patient p Where p.doctor.speciality=?1")
	List<Patient> getByDoctorSpeciality(Speciality speciality);
}