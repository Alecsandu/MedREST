package com.example.medrest.service;

import com.example.medrest.model.Location;
import com.example.medrest.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

public class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;
    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Good path when we have departments stored in the database")
    void getAllLocationsNotNullFlow() {
        Location location = new Location();
        List<Location> locations = new ArrayList<>();
        locations.add(location);

        when(locationRepository.findAll()).thenReturn(locations);
        List<Location> result = locationService.getAllLocations();

        assertNotNull(result);
        assertEquals(locations, result);
    }

    @Test
    @DisplayName("Good path when the department with the given id exists")
    void getLocationByIdNotNullFlow() {
        Location location = new Location();

        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        Location result = locationService.getLocationById(anyLong());

        assertNotNull(result);
        assertEquals(location, result);
    }

    @Test
    @DisplayName("Good path when the location is not null")
    void addLocationNotNullFlow() {
        Location location = new Location("Bucharest", "Nr.1", 89);
        Location savedLocation = new Location("Bucharest", "Nr.1", 89);
        savedLocation.setId(1L);

        when(locationRepository.save(location)).thenReturn(savedLocation);
        Location result = locationService.addLocation(location);

        assertNotNull(result);
        assertEquals(savedLocation.getId(), result.getId());
        assertEquals(savedLocation.getCity(), result.getCity());
        assertEquals(savedLocation.getStreet(), result.getStreet());
        assertEquals(savedLocation.getSpecialNumber(), result.getSpecialNumber());
    }

    @Test
    @DisplayName("Bad path when the given location is null")
    void addLocationNullFlow() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> locationService.addLocation(null));
        assertEquals("The given location doesn't contain any data",
                exception.getMessage());
    }

    @Test
    @DisplayName("Good path when the Location model is not null")
    void updateLocationNotNullFlow() {
        Location location = new Location();

        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        Boolean result = locationService.updateLocation(anyLong(), location);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    @DisplayName("Bad path when the Location model is null")
    void updateLocationNullFlow() {
        Location location = new Location();

        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Boolean result = locationService.updateLocation(anyLong(), location);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    @DisplayName("Patch location, good path when the location to be patched exists in the database")
    void patchLocationWhenTheLocationWithGivenIdExists() {
        Location location = new Location();

        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(locationRepository.save(location)).thenReturn(location);
        Boolean result = locationService.patchLocation(anyLong(), location);

        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    @DisplayName("Good path when the id is not null and the location actually exists")
    void deleteLocationWhenIdIsValidAndLocationExists() {
        Location location = new Location();

        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        doNothing().when(locationRepository).deleteById(anyLong());
        Boolean result = locationService.deleteLocation(anyLong());

        assertNotNull(result);
        assertTrue(result);
    }
}