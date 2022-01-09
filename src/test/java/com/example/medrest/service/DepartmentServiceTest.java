package com.example.medrest.service;

import com.example.medrest.exception.DepartmentNotFoundException;
import com.example.medrest.model.Department;
import com.example.medrest.repository.DepartmentRepository;
import com.example.medrest.repository.DoctorRepository;
import com.example.medrest.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Good path when the department with the given id exists")
    void getDepartmentIsPresentTruePath() {
        //arrange
        Department department = new Department();
        when(departmentRepository.findById(any()))
                .thenReturn(Optional.of(department));

        //act
        Department result = departmentService.getDepartment(anyLong());

        //assert
        assertNotNull(result);
        assertEquals(department, result);
    }

    @Test
    @DisplayName("Bad path when the department with the given id doesn't exist")
    void getDepartmentIsPresentFalsePath() {
        //arrange
        when(departmentRepository.findById(any()))
                .thenReturn(Optional.empty());

        //act
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> departmentService.getDepartment(anyLong()));

        //assert
        assertEquals("The department with the given id was not found!",
                exception.getMessage());
    }
}