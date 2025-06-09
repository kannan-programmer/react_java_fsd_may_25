package com.springboot.codingchallenge.service;

import org.springframework.stereotype.Service;

import com.springboot.codingchallenge.exception.ResourceNotFoundException;
import com.springboot.codingchallenge.model.MedicalHistory;
import com.springboot.codingchallenge.model.Patient;
import com.springboot.codingchallenge.repository.MedicalHistoryRepository;
import com.springboot.codingchallenge.repository.PatientRepository;

import java.util.List;

@Service
public class MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private PatientRepository patientRepo;

    

    public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository, PatientRepository patientRepo) {
		super();
		this.medicalHistoryRepository = medicalHistoryRepository;
		this.patientRepo = patientRepo;
	}



	public MedicalHistory addHistory(int patientId, MedicalHistory medicalHistory) throws ResourceNotFoundException {
        Patient patient = patientRepo.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));
        medicalHistory.setPatient(patient);
        return medicalHistoryRepository.save(medicalHistory);
    }


}