package com.example.medrest.controller;

import com.example.medrest.dto.DoctorDto;
import com.example.medrest.dto.PrescriptionDto;
import com.example.medrest.dto.SpecialisationDto;
import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Doctor;
import com.example.medrest.model.Prescription;
import com.example.medrest.model.Specialisation;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrescriptionController.class)
@EnableWebMvc
class PrescriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrescriptionService prescriptionService;

    private static Prescription staticPrescription;
    private static List<Prescription> initialPrescriptionList;
    private Prescription testPrescription;

    @BeforeAll
    public static void setupStatic() {
        staticPrescription = new Prescription("Augmentin",
                35,
                1);
        initialPrescriptionList = new ArrayList<>();
        initialPrescriptionList.add(staticPrescription);
    }

    @BeforeEach
    public void setupNonStatic() {
        testPrescription = new Prescription("Augmentin",
                35,
                1);
    }


    @Test
    void getPrescriptions() throws Exception {
        String endpoint = "/api/prescriptions/infos";
        when(prescriptionService.getAllPrescriptions()).thenReturn(initialPrescriptionList);
        mockMvc.perform(get(endpoint)).andExpect(status().isOk());

        when(prescriptionService.getAllPrescriptions()).thenThrow(new NotFoundException("No prescription was found in the database"));
        mockMvc.perform(get(endpoint)).andExpect(status().isNotFound());
    }

    @Test
    void getPrescription() throws Exception {
        String endpoint = "/api/prescriptions/{id}";
        testPrescription.setId(1L);

        when(prescriptionService.getPrescriptionById(anyLong())).thenReturn(testPrescription);
        mockMvc.perform(get(endpoint, 1L)).andExpect(status().isOk());

        when(prescriptionService.getPrescriptionById(anyLong())).thenThrow(new NotFoundException("Prescription not found"));
        mockMvc.perform(get(endpoint, anyLong())).andExpect(status().isNotFound());
    }

    @Test
    void createPrescription() throws Exception {
        String endpoint = "/api/prescriptions";
        testPrescription.setId(1L);

        PrescriptionDto prescriptionDto = new PrescriptionDto("Augmentin",
                35,
                1);
        Prescription localPrescription = new Prescription("Augmentin",
                35,
                1);

        when(prescriptionService.addPrescription(localPrescription)).thenReturn(testPrescription);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(prescriptionDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void changePrescription() throws Exception {
        String endpoint = "/api/prescriptions/{id}";
        testPrescription.setId(1L);

        when(prescriptionService.updatePrescription(1L, testPrescription)).thenReturn(true);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testPrescription)))
                .andExpect(status().isNoContent());

        when(prescriptionService.updatePrescription(1L, testPrescription)).thenReturn(false);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testPrescription)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchPrescription() throws Exception {
        String endpoint = "/api/prescriptions/{id}";
        testPrescription.setId(1L);

        when(prescriptionService.patchPrescription(1L, testPrescription)).thenReturn(true);
        mockMvc.perform(patch(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testPrescription)))
                .andExpect(status().isNoContent());
    }

    @Test
    void removePrescription() throws Exception {
        String endpoint = "/api/prescriptions/{id}";
        testPrescription.setId(1L);

        when(prescriptionService.deletePrescription(1L)).thenReturn(true);
        mockMvc.perform(delete(endpoint, 1L)).andExpect(status().isNoContent());
    }
}