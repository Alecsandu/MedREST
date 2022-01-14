package com.example.medrest.controller;

import com.example.medrest.dto.DepartmentDto;
import com.example.medrest.exception.DepartmentNotFoundException;
import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Department;
import com.example.medrest.model.Location;
import com.example.medrest.service.DepartmentService;
import com.example.medrest.service.DoctorService;
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

@WebMvcTest(controllers = DepartmentController.class)
@EnableWebMvc
class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private LocationService departmentLocationService;
    @MockBean
    private DoctorService doctorService;

    private static Department staticDepartment;
    private static List<Department> initialDepartmentList;
    private Department testDepartment;
    private Location testLocation;

    @BeforeAll
    public static void setupStatic() {
        staticDepartment = new Department("Stomatologie");
        initialDepartmentList = new ArrayList<>();
        initialDepartmentList.add(staticDepartment);
    }

    @BeforeEach
    public void setupNonStatic() {
        testDepartment = new Department("Ploiesti");
    }

    @Test
    void getAllDepartmentNames() throws Exception {
        String endpoint = "/api/departments/names";
        when(departmentService.getAllDepartments()).thenReturn(initialDepartmentList);
        mockMvc.perform(get(endpoint)).andExpect(status().isOk());

        when(departmentService.getAllDepartments()).thenThrow(new NotFoundException("No departments in the database!"));
        mockMvc.perform(get(endpoint)).andExpect(status().isNotFound());
    }

    @Test
    void getDepartmentById() throws Exception {
        String endpoint = "/api/departments/{id}";
        testDepartment.setId(1L);

        when(departmentService.getDepartment(anyLong())).thenReturn(testDepartment);
        mockMvc.perform(get(endpoint, 1L)).andExpect(status().isOk());

        when(departmentService.getDepartment(anyLong())).thenThrow(new DepartmentNotFoundException());
        mockMvc.perform(get(endpoint, anyLong())).andExpect(status().isNotFound());
    }

    @Test
    void createDepartment() throws Exception {
        String endpoint = "/api/departments";
        testDepartment.setId(1L);

        DepartmentDto departmentDto = new DepartmentDto("Dermatology");
        Department localDepartment = new Department("Dermatology");

        when(departmentService.addDepartment(localDepartment)).thenReturn(testDepartment);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void changeDepartment() throws Exception {
        String endpoint = "/api/departments/{id}";
        testDepartment.setId(1L);

        when(departmentService.updateDepartment(1L, testDepartment)).thenReturn(true);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testDepartment)))
                .andExpect(status().isNoContent());

        when(departmentService.updateDepartment(1L, testDepartment)).thenReturn(false);
        mockMvc.perform(put(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testDepartment)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchDepartment() throws Exception {
        String endpoint = "/api/departments/{id}";
        testDepartment.setId(1L);

        when(departmentService.patchDepartment(1L, testDepartment)).thenReturn(true);
        mockMvc.perform(patch(endpoint, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(testDepartment)))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeDepartment() throws Exception {
        String endpoint = "/api/departments/{id}";
        testDepartment.setId(1L);

        when(doctorService.checkIfAnyDoctorIsAssignedToGivenDepartment(anyLong())).thenReturn(true);
        when(departmentService.deleteDepartment(1L)).thenReturn(true);
        mockMvc.perform(delete(endpoint, 1L)).andExpect(status().isNoContent());
    }

    @Test
    void setDepartmentLocation() {
    }
}