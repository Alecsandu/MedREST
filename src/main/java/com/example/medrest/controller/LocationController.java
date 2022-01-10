package com.example.medrest.controller;

import com.example.medrest.dto.DepartmentDto;
import com.example.medrest.dto.LocationDto;
import com.example.medrest.mapper.LocationMapper;
import com.example.medrest.model.Location;
import com.example.medrest.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(@Autowired LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Get information about all the locations",
            operationId = "getLocations",
            description = "This request provides us the data of each Location entity stored in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locations were found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DepartmentDto.class)))}
            ),
            @ApiResponse(responseCode = "404", description = "No locations are stored in the database")
    })
    @GetMapping(value = "/infos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LocationDto>> getLocations() {
        return ResponseEntity.ok(locationService.getAllLocations().stream().map(LocationMapper::locationToLocationDto).collect(Collectors.toList()));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> getLocation(@PathVariable("id") Long id) {
        LocationDto locationDto = LocationMapper.locationToLocationDto(locationService.getLocationById(id));
        return ResponseEntity.ok(locationDto);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addLocation(String city,String street, Integer number) {
        Location newLocation = locationService.addLocation(new Location(city, street, number));
        URI uri = URI.create("api/locations/" + newLocation.getId());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> changeLocation(@PathVariable Long id, @RequestBody Location locationEntity) {
        Boolean isOperationSuccessful = locationService.updateLocation(id, locationEntity);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> patchLocation(@PathVariable("id") Long id, @RequestBody Location locationEntity) {
        Boolean isOperationSuccessful = locationService.patchLocation(id, locationEntity);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLocation(@PathVariable("id") Long id) {
        Boolean isOperationSuccessful = locationService.deleteLocation(id);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
