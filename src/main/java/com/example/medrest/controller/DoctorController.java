package com.example.medrest.controller;

import com.example.medrest.dto.DoctorDto;
import com.example.medrest.mapper.DoctorMapper;
import com.example.medrest.model.Doctor;
import com.example.medrest.service.DoctorService;
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
@RequestMapping("api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(@Autowired DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = "Get information about all the doctors",
            operationId = "getDoctors",
            description = "This request provides data about each doctor entity stored in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "doctors were found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DoctorDto.class)))}
            ),
            @ApiResponse(responseCode = "404", description = "No doctors are stored in the database"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/infos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DoctorDto>> getDoctors() {
        List<Doctor> doctorList = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctorList
                .stream()
                .map(DoctorMapper::doctorToDoctorDto)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get doctor using an id",
            operationId = "getDoctor",
            description = "By using a valid id you can get the information about its corresponding doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "404", description = "doctor not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DoctorDto> getDoctor(@PathVariable("id") Long id) {
        DoctorDto doctorDto = DoctorMapper.doctorToDoctorDto(doctorService.getDoctorById(id));
        return ResponseEntity.ok(doctorDto);
    }

    @Operation(summary = "Create a new doctor",
            operationId = "createDoctor",
            description = "By providing the basic values for a doctor entity, you can create one")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Doctor entity was created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createDoctor(@RequestBody @Valid Doctor newDoctor) {
        Doctor toBeSavedDoctor = new Doctor(newDoctor.getName(), newDoctor.getSalary());
        Doctor savedDoctor = doctorService.addDoctor(toBeSavedDoctor);
        URI uri = URI.create("api/doctors/" + savedDoctor.getId());
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update a doctor",
            operationId = "changeDoctor",
            description = "Change the information about a doctor by providing an id and new data")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "doctor was updated"),
            @ApiResponse(responseCode = "404", description = "doctor not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        Boolean isOperationSuccessful = doctorService.updateDoctor(id, doctor);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Patch a doctor entity",
            operationId = "patchDoctor",
            description = "A part of the information about doctor entity can be changed")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "doctor was patched"),
            @ApiResponse(responseCode = "404", description = "doctor not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> patchDoctor(@PathVariable("id") Long id, @RequestBody Doctor doctor) {
        Boolean isOperationSuccessful = doctorService.patchDoctor(id, doctor);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a doctor",
            operationId = "removeDoctor",
            description = "This endpoint removes a doctor from the database when an id is provided")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "doctor was deleted"),
            @ApiResponse(responseCode = "404", description = "doctor not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDoctor(@PathVariable("id") Long id) {
        Boolean isOperationSuccessful = doctorService.deleteDoctor(id);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
