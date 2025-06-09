package com.springboot.codingchallenge.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.codingchallenge.dto.PatientDTO;
import com.springboot.codingchallenge.enums.Speciality;
import com.springboot.codingchallenge.exception.ResourceNotFoundException;
import com.springboot.codingchallenge.model.Doctor;
import com.springboot.codingchallenge.model.MedicalHistory;
import com.springboot.codingchallenge.model.Patient;
import com.springboot.codingchallenge.model.User;
import com.springboot.codingchallenge.repository.DoctorRepository;
import com.springboot.codingchallenge.repository.PatientRepository;
import com.springboot.codingchallenge.repository.UserRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepo;
    private final UserRepository userRepo;
    private final DoctorRepository doctorRepo;
    private final MedicalHistoryService medicalHistoryService;
    private final PatientDTO patientDTO;

    public PatientService(PatientRepository patientRepo, UserRepository userRepo, DoctorRepository doctorRepo,
                          MedicalHistoryService medicalHistoryService, PatientDTO patientDTO) {
        this.patientRepo = patientRepo;
        this.userRepo = userRepo;
        this.doctorRepo = doctorRepo;
        this.medicalHistoryService = medicalHistoryService;
        this.patientDTO = patientDTO;
    }

    public Patient registerPatient(int userId, Patient patient) throws ResourceNotFoundException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (!"PATIENT".equalsIgnoreCase(user.getRole())) {
            throw new IllegalArgumentException("Only users with role PATIENT can register.");
        }

        patient.setUser(user);

        // Set patient reference inside each medical history before saving
        if (patient.getMedicalHistory() != null) {
            for (MedicalHistory medicalHistory : patient.getMedicalHistory()) {
                medicalHistory.setPatient(patient);
            }
        }

        return patientRepo.save(patient);
    }

    public void makeAppointment(int patientId, int doctorId) throws ResourceNotFoundException {
        Patient patient = getEntityById(patientId);
        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        patient.setDoctor(doctor);
        patientRepo.save(patient);
    }
    
    public List<PatientDTO> getPatientsByDoctorId(int doctorId) {
        List<Patient> patients = patientRepo.findByDoctorId(doctorId);
        List<PatientDTO> dtoList = new ArrayList<>();

        for (Patient patient : patients) {
            dtoList.add(patientDTO.convertPatientToDto(patient));
        }

        return dtoList;
    }

    public PatientDTO getById(int id) throws ResourceNotFoundException {
        Patient patient = getEntityById(id);
        return patientDTO.convertPatientToDto(patient);
    }

    public Patient getEntityById(int id) throws ResourceNotFoundException {
        return patientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

	public List<Patient> getByDoctorSpeciality(String speciality) {
		Speciality enumSpeciality = Speciality.valueOf(speciality.toUpperCase());
		return patientRepo.getByDoctorSpeciality(enumSpeciality);
	}
}
