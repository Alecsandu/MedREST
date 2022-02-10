package com.example.medrest.controller;

import com.example.medrest.dto.LocationDto;
import com.example.medrest.exception.CanNotDeleteException;
import com.example.medrest.mapper.LocationMapper;
import com.example.medrest.model.Location;
import com.example.medrest.service.DepartmentService;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/locations")
public class LocationController {
    private final LocationService locationService;
    private final DepartmentService departmentService;

    public LocationController(@Autowired LocationService locationService,
                              @Autowired DepartmentService departmentService) {
        this.locationService = locationService;
        this.departmentService = departmentService;
    }

    @Operation(summary = "Get information about all the locations",
            operationId = "getLocations",
            description = "This request provides us the data of each Location entity stored in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locations were found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = LocationDto.class)))}
            ),
            @ApiResponse(responseCode = "404", description = "No locations are stored in the database"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/infos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LocationDto>> getLocations() {
        List<Location> locationList = locationService.getAllLocations();
        return ResponseEntity.ok(locationList
                .stream()
                .map(LocationMapper::locationToLocationDto)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get location using an id",
            operationId = "getLocation",
            description = "By using a valid id you can get the information about its corresponding location")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Location found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = LocationDto.class))}),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> getLocation(@PathVariable("id") Long id) {
        LocationDto locationDto = LocationMapper.locationToLocationDto(locationService.getLocationById(id));
        return ResponseEntity.ok(locationDto);
    }

    @Operation(summary = "Create a new location",
            operationId = "createLocation",
            description = "By providing the basic values for a location you can create one")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Location was added",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = LocationDto.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createLocation(@Valid @RequestBody LocationDto newLocation) {
        Location savedLocation = locationService.addLocation(LocationMapper.locationDtoToLocation(newLocation));
        URI uri = URI.create("api/locations/" + savedLocation.getId());
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update a location",
            operationId = "changeLocation",
            description = "Change the information about a location by providing an id and new data")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Location was updated"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeLocation(@PathVariable Long id, @Valid @RequestBody Location locationEntity) {
        Boolean isOperationSuccessful = locationService.updateLocation(id, locationEntity);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Patch a location",
            operationId = "patchLocation",
            description = "A part of the information about location can be changed")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Location was patched"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> patchLocation(@PathVariable("id") Long id, @Valid @RequestBody Location locationEntity) {
        Boolean isOperationSuccessful = locationService.patchLocation(id, locationEntity);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a Location",
            operationId = "removeLocation",
            description = "This endpoint removes a location from the database when an id is provided")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Location was deleted"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "409", description = "The location can no be deleted because it has departments set to it"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLocation(@PathVariable("id") Long id) {
        Boolean canBeRemoved = departmentService.checkIfAnyDepartmentHasGivenLocation(id);
        if (!canBeRemoved) {
            throw new CanNotDeleteException("The location can no be deleted because it has departments set to it");
        }
        Boolean isOperationSuccessful = locationService.deleteLocation(id);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
