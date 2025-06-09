package com.springboot.codingchallenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.codingchallenge.exception.ResourceNotFoundException;
import com.springboot.codingchallenge.model.Doctor;
import com.springboot.codingchallenge.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/add/{userid}")
    public ResponseEntity<Doctor> addDoctor(@PathVariable int userid, @RequestBody Doctor doctor) throws ResourceNotFoundException {
        return ResponseEntity.ok(doctorService.addDoctor(userid,doctor));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable int id) throws ResourceNotFoundException {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/getBySpecialization/{speciality}")
    public ResponseEntity<List<Doctor>> getBySpecialization(@PathVariable String speciality) throws ResourceNotFoundException {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(speciality));
    }

    
}