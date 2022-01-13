package com.example.medrest.controller;

import com.example.medrest.dto.DoctorDto;
import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Doctor;
import com.example.medrest.service.DoctorService;
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

@WebMvcTest(controllers = DoctorController.class)
@EnableWebMvc
class DoctorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    private static Doctor staticDoctor;
    private static List<Doctor> initialDoctorList;
    private Doctor testDoctor;

    @BeforeAll
    public static void setupStatic() {
        staticDoctor = new Doctor("Gelu Andrei", 10000);
        initialDoctorList = new ArrayList<>();
        initialDoctorList.add(staticDoctor);
    }

    @BeforeEach
    public void setupNonStatic() {
        testDoctor = new Doctor("Bucur Andrei", 10000);
    }


    @Test
    void getDoctors() throws Exception {
        String endpoint = "/api/doctors/infos";
        when(doctorService.getAllDoctors()).thenReturn(initialDoctorList);
        mockMvc.perform(get(endpoint)).andExpect(status().isOk());

        when(doctorService.getAllDoctors()).thenThrow(new NotFoundException("No doctors were found!"));
        mockMvc.perform(get(endpoint)).andExpect(status().isNotFound());
    }

    @Test
    void getDoctor() throws Exception {
        String endpoint = "/api/doctors/{id}";
        testDoctor.setId(1L);

        when(doctorService.getDoctorById(anyLong())).thenReturn(testDoctor);
        mockMvc.perform(get(endpoint, 1L)).andExpect(status().isOk());

        when(doctorService.getDoctorById(anyLong())).thenThrow(new NotFoundException("No doctor with the given id was found!"));
        mockMvc.perform(get(endpoint, anyLong())).andExpect(status().isNotFound());
    }

    @Test
    void createDoctor() throws Exception {
        String endpoint = "/api/doctors";
        testDoctor.setId(1L);

        DoctorDto doctorDto = new DoctorDto("Bucur Andrei", 10000);
        Doctor localDoctor = new Doctor("Bucur Andrei", 10000);

        when(doctorService.addDoctor(localDoctor)).thenReturn(testDoctor);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(doctorDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void changeDoctor() throws Exception {
        String endpoint = "/api/doctors/{id}";
        testDoctor.setId(1L);

        when(doctorService.updateDoctor(1L, testDoctor)).thenReturn(true);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testDoctor)))
                .andExpect(status().isNoContent());

        when(doctorService.updateDoctor(1L, testDoctor)).thenReturn(false);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testDoctor)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchDoctor() throws Exception {
        String endpoint = "/api/doctors/{id}";
        testDoctor.setId(1L);

        when(doctorService.patchDoctor(1L, testDoctor)).thenReturn(true);
        mockMvc.perform(patch(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testDoctor)))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeDoctor() throws Exception {
        String endpoint = "/api/doctors/{id}";
        testDoctor.setId(1L);

        when(doctorService.deleteDoctor(1L)).thenReturn(true);
        mockMvc.perform(delete(endpoint, 1L)).andExpect(status().isNoContent());
    }
}