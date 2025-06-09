package com.springboot.codingchallenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.codingchallenge.dto.PatientDTO;
import com.springboot.codingchallenge.exception.ResourceNotFoundException;
import com.springboot.codingchallenge.model.Patient;
import com.springboot.codingchallenge.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    //  Register a patient
    @PostMapping("/register/{userId}")
    public ResponseEntity<Patient> registerPatient(@PathVariable int userId, @RequestBody Patient patient) throws ResourceNotFoundException {
        Patient saved = patientService.registerPatient(userId, patient);
        return ResponseEntity.ok(saved);
    }

    // Make an appointment
    @PutMapping("/appointment/{patientId}/{doctorId}")
    public ResponseEntity<String> makeAppointment(@PathVariable int patientId, @PathVariable int doctorId) throws ResourceNotFoundException {
        patientService.makeAppointment(patientId, doctorId);
        return ResponseEntity.ok("Appointment made successfully.");
    }

    // Get all patients by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PatientDTO>> getPatientsByDoctorId(@PathVariable int doctorId) {
        List<PatientDTO> patientList = patientService.getPatientsByDoctorId(doctorId);
        return ResponseEntity.ok(patientList);
    }

    // Get patient by ID
    @GetMapping("/getpateintbyId/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable int id) throws ResourceNotFoundException {
        PatientDTO dto = patientService.getById(id);
        return ResponseEntity.ok(dto);
    }
    
    // Get by doctor Speciality
    @GetMapping("/getBySpeciality/{speciality}")
    public List<Patient> getByDoctorSpeciality(@PathVariable String speciality){
		return patientService.getByDoctorSpeciality(speciality);
    	
    }
}