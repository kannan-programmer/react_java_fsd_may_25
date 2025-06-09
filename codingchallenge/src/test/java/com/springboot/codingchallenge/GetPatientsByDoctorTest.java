package com.springboot.codingchallenge;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import com.springboot.codingchallenge.dto.PatientDTO;
import com.springboot.codingchallenge.model.MedicalHistory;
import com.springboot.codingchallenge.model.Patient;
import com.springboot.codingchallenge.repository.PatientRepository;
import com.springboot.codingchallenge.service.PatientService;

import org.junit.jupiter.api.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GetPatientsByDoctorTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientDTO patientDTO;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private MedicalHistory history;
    private PatientDTO dto;

    AutoCloseable openMocks;

    @BeforeEach
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        // Setup Patient
        patient = new Patient();
        patient.setId(101);
        patient.setName("Kannan");
        patient.setAge(30);

        // Setup MedicalHistory
        history = new MedicalHistory();
        history.setIllness("Fever");
        history.setNumOfYears(3);
        history.setCurrentMedication("Injection");

        patient.setMedicalHistory(List.of(history));

        // Setup DTO
        dto = new PatientDTO();
        dto.setName("Kannan");
        dto.setAge(30);
        dto.setMedicalHistories(List.of(history));
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();

        patient = null;
        history = null;
        dto = null;
    }

    @Test
    public void testGetPatientsByDoctorId_ReturnsPatientDTOList() {
        int doctorId = 1;

        when(patientRepository.findByDoctorId(doctorId)).thenReturn(List.of(patient));
        when(patientDTO.convertPatientToDto(patient)).thenReturn(dto);

        List<PatientDTO> result = patientService.getPatientsByDoctorId(doctorId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Kannan", result.get(0).getName());
        assertEquals(30, result.get(0).getAge());
        assertEquals("Fever", result.get(0).getMedicalHistories().get(0).getIllness());

        verify(patientRepository, times(1)).findByDoctorId(doctorId);
        verify(patientDTO, times(1)).convertPatientToDto(patient);
    }
}