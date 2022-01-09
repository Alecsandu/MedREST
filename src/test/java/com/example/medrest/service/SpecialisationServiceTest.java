package com.example.medrest.service;

import com.example.medrest.model.Location;
import com.example.medrest.model.Specialisation;
import com.example.medrest.repository.SpecialisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class SpecialisationServiceTest {
    @Mock
    private SpecialisationRepository specialisationRepository;
    @InjectMocks
    private SpecialisationService specialisationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Good path when we have specialisations stored in the database")
    void getSpecializationsNotNullFlow() {
        Specialisation specialisation = new Specialisation();
        List<Specialisation> specialisations = new ArrayList<>();
        specialisations.add(specialisation);
        List<Specialisation> testSpecialisations = new ArrayList<>();
        testSpecialisations.add(specialisation);

        when(specialisationRepository.findAll()).thenReturn(specialisations);

        List<Specialisation> result = specialisationService.getSpecializations();

        assertNotNull(result);
        assertEquals(testSpecialisations, result);
    }

    @Test
    @DisplayName("Good path when the department with the given id exists")
    void getSpecialisationByIdNotNullFlow() {
        Specialisation specialisation = new Specialisation();

        when(specialisationRepository.findById(anyLong())).thenReturn(Optional.of(specialisation));
        Specialisation result = specialisationService.getSpecialisationById(anyLong());

        assertNotNull(result);
        assertEquals(specialisation, result);
    }

    @Test
    @DisplayName("Good path when the specialisation is not null")
    void addSpecialisationNotNullFlow() {
        Specialisation specialisation = new Specialisation("Dermatology", 100, 1000);
        Specialisation testSpecialisation = new Specialisation("Dermatology", 100, 1000);
        testSpecialisation.setId(1L);

        when(specialisationRepository.save(specialisation)).thenReturn(testSpecialisation);
        Specialisation result = specialisationService.addSpecialisation(specialisation);

        assertNotNull(result);
        assertEquals(testSpecialisation.getId(), result.getId());
        assertEquals(testSpecialisation.getName(), result.getName());
        assertEquals(testSpecialisation.getMinSalary(), result.getMinSalary());
        assertEquals(testSpecialisation.getMaxSalary(), result.getMaxSalary());
    }

    @Test
    @DisplayName("Bad path when the given specialisation is null")
    void addSpecialisationNullFlow() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> specialisationService.addSpecialisation(null));
        assertEquals("The given specialisation is null",
                exception.getMessage());
    }

    @Test
    @DisplayName("Bad path, ie. when the given specialisation isn't in the database")
    void updateSpecialisationNullFlow() {
        when(specialisationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Boolean result = specialisationService.updateSpecialisation(anyLong(),
                new Specialisation());

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    @DisplayName("Patch specialisation, good path when the specialisation to be patched exists in the database")
    void patchSpecialisationWhenTheSpecialisationWithGivenIdExists() {
        Specialisation specialisation = new Specialisation();
        when(specialisationRepository.findById(anyLong())).thenReturn(Optional.of(specialisation));
        when(specialisationRepository.save(specialisation)).thenReturn(specialisation);

        Boolean result = specialisationService.patchSpecialisation(anyLong(), specialisation);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    @DisplayName("Good path when the specialisation actually exists")
    void deleteSpecialisationWhenSpecialisationExists() {
        Specialisation specialisation = new Specialisation();

        when(specialisationRepository.findById(anyLong())).thenReturn(Optional.of(specialisation));
        doNothing().when(specialisationRepository).deleteById(anyLong());
        Boolean result = specialisationService.deleteSpecialisation(anyLong());

        assertNotNull(result);
        assertTrue(result);
    }
}