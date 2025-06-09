package com.springboot.codingchallenge.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.codingchallenge.enums.Speciality;
import com.springboot.codingchallenge.exception.ResourceNotFoundException;
import com.springboot.codingchallenge.model.Doctor;
import com.springboot.codingchallenge.model.User;
import com.springboot.codingchallenge.repository.DoctorRepository;
import com.springboot.codingchallenge.repository.UserRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private UserRepository userRepo;

    

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepo) {
		super();
		this.doctorRepository = doctorRepository;
		this.userRepo = userRepo;
	}


	public Doctor addDoctor(int userid, Doctor doctor) throws ResourceNotFoundException {
		User user = userRepo.findById(userid).orElseThrow(()-> new ResourceNotFoundException("user Not found"));
		doctor.setUser(user);
		return doctorRepository.save(doctor);
	}

    public Doctor getDoctorById(int id) throws ResourceNotFoundException {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
    }

    public List<Doctor> getAllDoctors() {
        return (List<Doctor>) doctorRepository.findAll();
    }

    public List<Doctor> getDoctorsBySpecialization(String speciality) throws ResourceNotFoundException {
        try {
            Speciality enumSpeciality = Speciality.valueOf(speciality.toUpperCase());
            return doctorRepository.findBySpecialization(enumSpeciality);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid speciality: " + speciality);
        }
    }


    public void deleteDoctor(int id) throws ResourceNotFoundException {
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
    }



}