package com.example.medrest.controller;

import com.example.medrest.dto.LocationDto;
import com.example.medrest.dto.SpecialisationDto;
import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Location;
import com.example.medrest.service.DepartmentService;
import com.example.medrest.service.LocationService;
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

@WebMvcTest(controllers = LocationController.class)
@EnableWebMvc
public class LocationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocationService locationService;
    @MockBean
    private DepartmentService departmentService;

    private static Location staticLocation;
    private static List<Location> initialLocationList;
    private Location testLocation;

    @BeforeAll
    public static void setupStatic() {
        staticLocation = new Location("Bucharest",
                "Victoriei",
                13);
        initialLocationList = new ArrayList<>();
        initialLocationList.add(staticLocation);
    }

    @BeforeEach
    public void setupNonStatic() {
        testLocation = new Location("Ploiesti", "Republicii", 25);
    }

    @Test
    void testGetLocations() throws Exception {
        String endpoint = "/api/locations/infos";
        when(locationService.getAllLocations()).thenReturn(initialLocationList);
        mockMvc.perform(get(endpoint)).andExpect(status().isOk());

        when(locationService.getAllLocations()).thenThrow(new NotFoundException("No locations were found!"));
        mockMvc.perform(get(endpoint)).andExpect(status().isNotFound());
    }

    @Test
    void testGetLocation() throws Exception {
        String endpoint = "/api/locations/{id}";
        testLocation.setId(1L);

        when(locationService.getLocationById(anyLong())).thenReturn(testLocation);
        mockMvc.perform(get(endpoint, 1L)).andExpect(status().isOk());

        when(locationService.getLocationById(anyLong())).thenThrow(new NotFoundException("No location with the given id was found!"));
        mockMvc.perform(get(endpoint, anyLong())).andExpect(status().isNotFound());
    }

    @Test
    void testCreateLocation() throws Exception {
        String endpoint = "/api/locations";
        LocationDto locationDto = new LocationDto("Ploiesti", "Republicii", 25);
        testLocation.setId(1L);

        when(locationService.addLocation(testLocation)).thenReturn(testLocation);
        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testChangeLocation() throws Exception {
        String endpoint = "/api/locations/{id}";
        testLocation.setId(1L);

        when(locationService.updateLocation(1L, testLocation)).thenReturn(true);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testLocation)))
                .andExpect(status().isNoContent());

        when(locationService.updateLocation(1L, testLocation)).thenReturn(false);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testLocation)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatchLocation() throws Exception {
        String endpoint = "/api/locations/{id}";
        testLocation.setId(1L);

        when(locationService.patchLocation(1L, testLocation)).thenReturn(true);
        mockMvc.perform(patch(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testLocation)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemoveLocation() throws Exception {
        String endpoint = "/api/locations/{id}";
        testLocation.setId(1L);

        when(departmentService.checkIfAnyDepartmentHasGivenLocation(anyLong())).thenReturn(true);
        when(locationService.deleteLocation(1L)).thenReturn(true);
        mockMvc.perform(delete(endpoint, 1L)).andExpect(status().isNoContent());
    }
}