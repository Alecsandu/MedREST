package com.example.medrest.controller;

import com.example.medrest.dto.SpecialisationDto;
import com.example.medrest.exception.NotFoundException;
import com.example.medrest.mapper.SpecialisationMapper;
import com.example.medrest.model.Specialisation;
import com.example.medrest.service.SpecialisationService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

@WebMvcTest(controllers = SpecialisationController.class)
@EnableWebMvc
class SpecialisationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpecialisationService specialisationService;

    private static Specialisation staticSpecialisation;
    private static List<Specialisation> initialSpecialisationList;
    private Specialisation testSpecialisation;

    @BeforeAll
    public static void setupStatic() {
        staticSpecialisation = new Specialisation("Dermatology", 1000, 9000);
        initialSpecialisationList = new ArrayList<>();
        initialSpecialisationList.add(staticSpecialisation);
    }

    @BeforeEach
    public void setupNonStatic() {
        testSpecialisation = new Specialisation("Dermatology", 1000, 9000);
    }


    @Test
    void getSpecialisations() throws Exception {
        String endpoint = "/api/specialisations/infos";
        when(specialisationService.getSpecializations()).thenReturn(initialSpecialisationList);
        mockMvc.perform(get(endpoint)).andExpect(status().isOk());

        when(specialisationService.getSpecializations()).thenThrow(new NotFoundException("No specialization was found in the database!"));
        mockMvc.perform(get(endpoint)).andExpect(status().isNotFound());
    }

    @Test
    void getSpecialisation() throws Exception {
        String endpoint = "/api/specialisations/{id}";
        testSpecialisation.setId(1L);

        when(specialisationService.getSpecialisationById(anyLong())).thenReturn(testSpecialisation);
        mockMvc.perform(get(endpoint, 1L)).andExpect(status().isOk());

        when(specialisationService.getSpecialisationById(anyLong())).thenThrow(new NotFoundException("Specialisation not found"));
        mockMvc.perform(get(endpoint, anyLong())).andExpect(status().isNotFound());
    }

    @Test
    void createSpecialisation() throws Exception {
        String endpoint = "/api/specialisations";
        testSpecialisation.setId(1L);

        SpecialisationDto specialisationDto = new SpecialisationDto("Dermatology", 1000, 9000);
        Specialisation localSpecialisation = new Specialisation("Dermatology", 1000, 9000);

        when(specialisationService.addSpecialisation(localSpecialisation)).thenReturn(testSpecialisation);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(specialisationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void changeSpecialisation() throws Exception {
        String endpoint = "/api/specialisations/{id}";
        testSpecialisation.setId(1L);

        when(specialisationService.updateSpecialisation(1L, testSpecialisation)).thenReturn(true);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testSpecialisation)))
                .andExpect(status().isNoContent());

        when(specialisationService.updateSpecialisation(1L, testSpecialisation)).thenReturn(false);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testSpecialisation)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchSpecialisation() throws Exception {
        String endpoint = "/api/specialisations/{id}";
        testSpecialisation.setId(1L);

        when(specialisationService.patchSpecialisation(1L, testSpecialisation)).thenReturn(true);
        mockMvc.perform(patch(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testSpecialisation)))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeSpecialisation() throws Exception {
        String endpoint = "/api/specialisations/{id}";
        testSpecialisation.setId(1L);

        when(specialisationService.deleteSpecialisation(1L)).thenReturn(true);
        mockMvc.perform(delete(endpoint, 1L)).andExpect(status().isNoContent());
    }
}