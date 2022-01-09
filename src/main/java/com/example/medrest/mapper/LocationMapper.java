package com.example.medrest.mapper;

import com.example.medrest.dto.LocationDto;
import com.example.medrest.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public static LocationDto locationToLocationDto(Location location) {
        return new LocationDto(location.getCity(), location.getStreet(), location.getSpecialNumber());
    }
}
