package com.example.medrest.controller;

import com.example.medrest.dto.LocationDto;
import com.example.medrest.mapper.LocationMapper;
import com.example.medrest.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(@Autowired LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/infos")
    public ResponseEntity<List<LocationDto>> getLocations() {
        return ResponseEntity.ok(locationService.getAllLocations().stream().map(LocationMapper::locationToLocationDto).collect(Collectors.toList()));
    }
}
