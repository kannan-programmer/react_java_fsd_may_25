package com.springboot.codingchallenge.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.codingchallenge.exception.ResourceNotFoundException;
import com.springboot.codingchallenge.model.MedicalHistory;
import com.springboot.codingchallenge.service.MedicalHistoryService;


@RestController
@RequestMapping("/api/medicalhistory")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    // Add medical history for a patient
    @PostMapping("/add/{patientId}")
    public ResponseEntity<MedicalHistory> addHistory(@PathVariable int patientId, @RequestBody MedicalHistory medicalHistory) throws ResourceNotFoundException {
        MedicalHistory saved = medicalHistoryService.addHistory(patientId, medicalHistory);
        return ResponseEntity.ok(saved);
    }

    // get patienthistory by id
    
    
}