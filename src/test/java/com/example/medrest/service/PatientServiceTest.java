package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Doctor;
import com.example.medrest.model.Patient;
import com.example.medrest.model.Prescription;
import com.example.medrest.repository.PatientRepository;
import com.example.medrest.repository.PrescriptionRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PrescriptionRepository prescriptionRepository;
    @InjectMocks
    private PatientService patientService;

    @Test
    @DisplayName("Good path when we have data in the database about patients")
    void getAllPatientsIsPresentFlow() {
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        List<Patient> patientsList = new ArrayList<>();
        patientsList.add(patient1);
        patientsList.add(patient2);

        when(patientRepository.findAll()).thenReturn(patientsList);
        List<Patient> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(patientsList, result);
    }

    @Test
    @DisplayName("Good path when the database contains a patient with the given id")
    void getPatientByIdIsPresentTrue() {
        Patient patient = new Patient();

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        Patient result = patientService.getPatientById(anyLong());

        assertNotNull(result);
        assertEquals(patient, result);
    }

    @Test
    @DisplayName("Good path when the given patient data is not null")
    void addPatientGoodFlow() {
        Patient patient = new Patient("Val",
                "Andrei",
                "0730000000",
                "email@gmail.com");
        Patient testPatient = new Patient();
        testPatient.setId(1L);

        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);
        Patient result = patientService.addPatient(patient);

        assertNotNull(result);
        assertEquals(testPatient.getId(), result.getId());
        assertEquals(testPatient.getFirstName(), result.getFirstName());
        assertEquals(testPatient.getLastName(), result.getLastName());
        assertEquals(testPatient.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(testPatient.getEmailAddress(), result.getEmailAddress());
    }

    @Test
    @DisplayName("Good path when we can update the patient data")
    void updatePatientIsPresentTrueFlow() {
        Patient patient = new Patient();

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        Boolean result = patientService.updatePatient(anyLong(), patient);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    @DisplayName("Bad path when the patient data which we want to change doesnt exist in the database")
    void patchPatientIsPresentFalseFlow() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());
        Boolean result = patientService.patchPatient(anyLong(), null);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    @DisplayName("Bad path when we don't have data about the patient with the given id")
    void deletePatientButIdDoesNotReturnAnyData() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());
        Boolean result = patientService.deletePatient(anyLong());

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    @DisplayName("When both entities have exist(have valid ids) add prescription to the patient.")
    void addPrescriptionToPatientBothEntitiesExistFlow() {
        Patient patient = new Patient("Val",
                "Andrei",
                "0730000000",
                "email@gmail.com");
        patient.setId(1L);
        Prescription prescription = new Prescription("Augmentin",
                35,
                1);
        prescription.setId(1L);
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        patientService.addPrescriptionToPatient(1L, 1L);

        verify(prescriptionRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    @DisplayName("NotFoundException is thrown when the prescription does not exist.")
    void addPrescriptionFromPatientButPrescriptionDoesNotExistFlow() {
        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> patientService.addPrescriptionToPatient(1L, 1L));

        assertNotNull(exception);
        assertEquals("The prescription with the given id was not found!\n",
                exception.getMessage());
    }

    @Test
    @DisplayName("NotFoundException is thrown when the patient does not exist.")
    void addPatientPrescriptionsListButPatientDoesNotExistFlow() {
        Prescription prescription = new Prescription("Augmentin",
                35,
                1);
        prescription.setId(1L);

        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> patientService.addPrescriptionToPatient(1L, 1L));

        assertNotNull(exception.getMessage());
        assertEquals("The patient with the given id was not found!\n",
                exception.getMessage());
    }
}