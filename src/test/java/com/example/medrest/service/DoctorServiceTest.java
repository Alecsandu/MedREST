package com.example.medrest.service;

import com.example.medrest.model.Doctor;
import com.example.medrest.repository.DoctorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientService patientService;
    @Mock
    private SpecialisationService specialisationService;
    @Mock
    private DepartmentService departmentService;
    @InjectMocks
    private DoctorService doctorService;

    @Test
    @DisplayName("Good path when we have data in the database about doctors")
    void getAllDoctorsIsPresentTrueFlow() {
        Doctor doctor1 = new Doctor();
        Doctor doctor2 = new Doctor();
        List<Doctor> doctorsList = new ArrayList<>();
        doctorsList.add(doctor1);
        doctorsList.add(doctor2);

        when(doctorRepository.findAll()).thenReturn(doctorsList);
        List<Doctor> result = doctorService.getAllDoctors();

        assertNotNull(result);
        assertEquals(doctorsList, result);
    }

    @Test
    @DisplayName("Good path when the database contains a doctor with the given id")
    void getDoctorByIdIsPresentTrue() {
        Doctor doctor = new Doctor();

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        Doctor result = doctorService.getDoctorById(anyLong());

        assertNotNull(result);
        assertEquals(doctor, result);
    }

    @Test
    @DisplayName("Good path when the given doctor data is not null")
    void addDoctorGoodFlow() {
        Doctor doctor = new Doctor("Gelu Andrei", 10000);
        Doctor testDoctor = new Doctor("Gelu Andrei", 10000);
        testDoctor.setId(1L);

        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);
        Doctor result = doctorService.addDoctor(doctor);

        assertNotNull(result);
        assertEquals(testDoctor.getId(), result.getId());
        assertEquals(testDoctor.getName(), result.getName());
        assertEquals(testDoctor.getSalary(), result.getSalary());
    }

    @Test
    @DisplayName("Good path when we can update the doctor data")
    void updateDoctorIsPresentTrueFlow() {
        Doctor doctor = new Doctor();

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        Boolean result = doctorService.updateDoctor(anyLong(), doctor);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    @DisplayName("Bad path when the doctor data which we want to change doesnt exist in the database")
    void patchDoctorIsPresentFalseFlow() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());
        Boolean result = doctorService.patchDoctor(anyLong(), null);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    @DisplayName("Bad path when we don't have data about the doctor with the given id")
    void deleteDoctorButIdDoesNotReturnAnyData() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());
        Boolean result = doctorService.deleteDoctor(anyLong());

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    void testCheckIfAnyDoctorIsAssignedToGivenDepartmentGoodPath() {
        when(doctorRepository.findAll()).thenReturn(new ArrayList<Doctor>());
        Boolean result = doctorService.checkIfAnyDoctorIsAssignedToGivenDepartment(1L);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    void checkIfAnyDoctorHasSetGivenSpecialisation() {
        when(doctorRepository.findAll()).thenReturn(new ArrayList<Doctor>());
        Boolean result = doctorService.checkIfAnyDoctorHasSetGivenSpecialisation(1L);

        assertNotNull(result);
        assertTrue(result);
    }
}