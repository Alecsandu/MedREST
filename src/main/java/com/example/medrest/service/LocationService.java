package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Location;
import com.example.medrest.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    public final LocationRepository locationRepository;

    public LocationService(@Autowired LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        List<Location> locations = (List<Location>) locationRepository.findAll();
        if (!locations.isEmpty()) {
            return locations;
        } else {
            throw new NotFoundException("No locations were found!");
        }
    }

    public Location getLocationById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            return location.get();
        } else {
            throw new NotFoundException("No location with the given id was found!");
        }
    }

    public Location addLocation(Location location) {
        if (location != null) {
            return locationRepository.save(location);
        } else {
            throw new RuntimeException("The given location doesn't contain any data");
        }
    }

    public Boolean updateLocation(Long id, Location location) {
        Optional<Location> existingLocation = locationRepository.findById(id);
        if (existingLocation.isPresent()) {
            location.setId(id);
            locationRepository.save(location);
            return true;
        } else {
            return false;
        }
    }

    public Boolean patchLocation(Long id, Location location) {
        Optional<Location> existingLocation = locationRepository.findById(id);
        if (existingLocation.isPresent()) {
            existingLocation.get().patch(location);
            locationRepository.save(existingLocation.get());
            return true;
        } else {
            return false;
        }
    }

    public Boolean deleteLocation(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            locationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
