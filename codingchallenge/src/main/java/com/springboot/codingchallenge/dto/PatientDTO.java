package com.springboot.codingchallenge.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.codingchallenge.model.MedicalHistory;
import com.springboot.codingchallenge.model.Patient;

@Component
public class PatientDTO {

    private String name;
    private int age;
    private List<MedicalHistory> medicalHistories;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<MedicalHistory> getMedicalHistories() {
        return medicalHistories;
    }

    public void setMedicalHistories(List<MedicalHistory> medicalHistories) {
        this.medicalHistories = medicalHistories;
    }

    // Convert Patient entity to PatientDTO
    public PatientDTO convertPatientToDto(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setName(patient.getName());
        dto.setAge(patient.getAge());

        List<MedicalHistory> historyDTOList = new ArrayList<>();
        for (MedicalHistory history : patient.getMedicalHistory()) {
            MedicalHistory historyDTO = new MedicalHistory();
            historyDTO.setIllness(history.getIllness());
            historyDTO.setNumOfYears(history.getNumOfYears());
            historyDTO.setCurrentMedication(history.getCurrentMedication());
            historyDTOList.add(historyDTO);
        }

        dto.setMedicalHistories(historyDTOList);
        return dto;
    }
}