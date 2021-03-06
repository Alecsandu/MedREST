package com.example.medrest.controller;

import com.example.medrest.dto.PatientDto;
import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Doctor;
import com.example.medrest.model.Patient;
import com.example.medrest.model.Prescription;
import com.example.medrest.service.DoctorService;
import com.example.medrest.service.PatientService;
import com.example.medrest.service.PrescriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatientController.class)
@EnableWebMvc
class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;
    @MockBean
    private PrescriptionService prescriptionService;
    @MockBean
    private DoctorService doctorService;

    private static List<Patient> initialPatientList;
    private Patient testPatient;

    @BeforeAll
    public static void setupStatic() {
        Patient staticPatient = new Patient("Val",
                "Andrei",
                "0730000000",
                "email@gmail.com");
        initialPatientList = new ArrayList<>();
        initialPatientList.add(staticPatient);
    }

    @BeforeEach
    public void setupNonStatic() {
        testPatient = new Patient("Val",
                "Andrei",
                "0730000000",
                "email@gmail.com");
    }

    @Test
    void getPatients() throws Exception {
        String endpoint = "/api/patients/infos";
        when(patientService.getAllPatients()).thenReturn(initialPatientList);
        mockMvc.perform(get(endpoint)).andExpect(status().isOk());

        when(patientService.getAllPatients()).thenThrow(new NotFoundException("No patients found!"));
        mockMvc.perform(get(endpoint)).andExpect(status().isNotFound());
    }

    @Test
    void getPatient() throws Exception {
        String endpoint = "/api/patients/{id}";
        testPatient.setId(1L);

        when(patientService.getPatientById(anyLong())).thenReturn(testPatient);
        mockMvc.perform(get(endpoint, 1L)).andExpect(status().isOk());

        when(patientService.getPatientById(anyLong())).thenThrow(new NotFoundException("No patient with the given id was found!"));
        mockMvc.perform(get(endpoint, anyLong())).andExpect(status().isNotFound());
    }

    @Test
    void createPatient() throws Exception {
        String endpoint = "/api/patients";
        testPatient.setId(1L);

        PatientDto patientDto = new PatientDto("Val",
                "Andrei",
                "0730000000",
                "email@gmail.com");
        Patient localPatient = new Patient("Val",
                "Andrei",
                "0730000000",
                "email@gmail.com");

        when(patientService.addPatient(localPatient)).thenReturn(testPatient);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void changePatient() throws Exception {
        String endpoint = "/api/patients/{id}";
        testPatient.setId(1L);

        when(patientService.updatePatient(1L, testPatient)).thenReturn(true);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testPatient)))
                .andExpect(status().isNoContent());

        when(patientService.updatePatient(1L, testPatient)).thenReturn(false);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testPatient)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchPatient() throws Exception {
        String endpoint = "/api/patients/{id}";
        testPatient.setId(1L);

        when(patientService.patchPatient(1L, testPatient)).thenReturn(true);
        mockMvc.perform(patch(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testPatient)))
                .andExpect(status().isNoContent());
    }

    @Test
    void removePatient() throws Exception {
        String endpoint = "/api/patients/{id}";
        testPatient.setId(1L);

        when(patientService.deletePatient(1L)).thenReturn(true);
        mockMvc.perform(delete(endpoint, 1L)).andExpect(status().isNoContent());
    }

    @Test
    void addPrescriptionToPatient() throws Exception {
        String endpoint = "/api/patients/{patientId}/prescriptions/{prescriptionId}";
        testPatient.setId(1L);
        Prescription prescription = new Prescription("Augmentin",
                35,
                1);
        prescription.setId(1L);

        when(patientService.getPatientById(1L)).thenReturn(testPatient);
        when(prescriptionService.getPrescriptionById(1L)).thenReturn(prescription);
        when(patientService.updatePatient(1L, testPatient)).thenReturn(true);
        mockMvc.perform(post(endpoint, 1L, 1L)).andExpect(status().isNoContent());
    }

    @Test
    void getPatientPrescriptions() throws Exception {
        String endpoint = "/api/patients/{id}/prescriptions";
        testPatient.setId(1L);

        when(patientService.getPatientById(1L)).thenReturn(testPatient);
        mockMvc.perform(get(endpoint, 1L)).andExpect(status().isNoContent());
    }

    @Test
    void removePatientPrescription() throws Exception {
        String endpoint = "/api/patients/{patientId}/prescriptions/{prescriptionId}";
        testPatient.setId(1L);
        Prescription prescription = new Prescription("Augmentin",
                35,
                1);
        prescription.setId(1L);
        testPatient.addPrescription(prescription);
        prescription.addPatients(testPatient);

        when(patientService.getPatientById(1L)).thenReturn(testPatient);
        when(prescriptionService.getPrescriptionById(1L)).thenReturn(prescription);
        when(patientService.addPatient(testPatient)).thenReturn(testPatient);
        when(prescriptionService.addPrescription(prescription)).thenReturn(prescription);
        mockMvc.perform(delete(endpoint, 1L, 1L)).andExpect(status().isNoContent());
    }

    @Test
    void appointPatientToDoctor() throws Exception{
        String endpoint = "/api/patients/{patientId}/doctors/{doctorId}";
        testPatient.setId(1L);
        Doctor doctor = new Doctor("Gelu Andrei", 10000);
        doctor.setId(1L);

        when(patientService.getPatientById(1L)).thenReturn(testPatient);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(patientService.patchPatient(1L, testPatient)).thenReturn(true);
        mockMvc.perform(post(endpoint,1L, 1L)).andExpect(status().isNoContent());
    }

    @Test
    void getPatientAppointmentsWithDoctors() throws Exception{
        String endpoint = "/api/patients/{id}/appointments";
        testPatient.setId(1L);

        when(patientService.getPatientById(1L)).thenReturn(testPatient);
        mockMvc.perform(get(endpoint, 1L)).andExpect(status().isNoContent());
    }

    @Test
    void removePatientAppointment() throws Exception{
        String endpoint = "/api/patients/{patientId}/doctors/{doctorId}";
        testPatient.setId(1L);
        Doctor doctor = new Doctor("Gelu Andrei", 10000);
        doctor.setId(1L);

        when(patientService.getPatientById(1L)).thenReturn(testPatient);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(patientService.updatePatient(1L, testPatient)).thenReturn(true);
        mockMvc.perform(delete(endpoint, 1L,1L)).andExpect(status().isNoContent());
    }
}