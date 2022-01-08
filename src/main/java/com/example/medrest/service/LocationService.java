package com.example.medrest.service;

import com.example.medrest.repository.LocationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocationService {
    public final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
}
