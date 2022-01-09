package com.example.medrest.service;

import com.example.medrest.model.Prescription;
import com.example.medrest.repository.PrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PrescriptionServiceTest {
    @Mock
    private PrescriptionRepository prescriptionRepository;
    @InjectMocks
    private PrescriptionService prescriptionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Good path when we have prescriptions stored in the database")
    void getAllPrescriptionsNotNullFlow() {
        Prescription prescription = new Prescription();
        List<Prescription> prescriptions = new ArrayList<>();
        prescriptions.add(prescription);

        when(prescriptionRepository.findAll()).thenReturn(prescriptions);
        List<Prescription> result = prescriptionService.getAllPrescriptions();

        assertNotNull(result);
        assertEquals(prescriptions, result);
    }

    @Test
    @DisplayName("Good path when the prescription with the given id exists")
    void getPrescriptionByIdNotNullFlow() {
        Prescription prescription = new Prescription();

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        Prescription result = prescriptionService.getPrescriptionById(anyLong());

        assertNotNull(result);
        assertEquals(prescription, result);
    }

    @Test
    @DisplayName("Good path when the prescription is not null")
    void addPrescriptionNotNullFlow() {
        Prescription prescription = new Prescription("Augmentin", 35, 1);
        Prescription testPrescription = new Prescription("Augmentin", 35, 1);
        testPrescription.setId(2L);

        when(prescriptionRepository.save(prescription)).thenReturn(testPrescription);
        Prescription result = prescriptionService.addPrescription(prescription);

        assertNotNull(result);
        assertEquals(testPrescription.getId(), result.getId());
        assertEquals(testPrescription.getMedicamentName(), result.getMedicamentName());
        assertEquals(testPrescription.getPrice(), result.getPrice());
        assertEquals(testPrescription.getAmountToTake(), result.getAmountToTake());
    }

    @Test
    @DisplayName("Good path when the prescription is not null")
    void updatePrescriptionNotNullFlow() {
        Prescription prescription = new Prescription();

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);
        Boolean result = prescriptionService.updatePrescription(anyLong(), prescription);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    @DisplayName("Patch prescription, good path when the prescription to be patched exists in the database")
    void patchPrescriptionWhenThePrescriptionWithTheGivenIdExists() {
        Prescription prescription = new Prescription();

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);
        Boolean result = prescriptionService.patchPrescription(anyLong(), prescription);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    @DisplayName("Good path when the id is not null and the prescription actually exists")
    void deletePrescriptionWhenPrescriptionExists() {
        Prescription prescription = new Prescription();

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(prescription));
        doNothing().when(prescriptionRepository).deleteById(anyLong());
        Boolean result = prescriptionService.deletePrescription(anyLong());

        assertNotNull(result);
        assertTrue(result);
    }
}