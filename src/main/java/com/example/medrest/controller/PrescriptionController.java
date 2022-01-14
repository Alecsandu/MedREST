package com.example.medrest.controller;

import com.example.medrest.dto.PatientDto;
import com.example.medrest.dto.PrescriptionDto;
import com.example.medrest.mapper.PatientMapper;
import com.example.medrest.mapper.PrescriptionMapper;
import com.example.medrest.model.Patient;
import com.example.medrest.model.Prescription;
import com.example.medrest.service.PatientService;
import com.example.medrest.service.PrescriptionService;
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
@RequestMapping("api/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    private final PatientService patientService;

    public PrescriptionController(@Autowired PrescriptionService prescriptionService,
                                  @Autowired PatientService patientService) {
        this.prescriptionService = prescriptionService;
        this.patientService = patientService;
    }

    @Operation(summary = "Get information about all the prescriptions",
            operationId = "getPrescriptions",
            description = "This request provides data about each prescriptions entity stored in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescriptions were found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PrescriptionDto.class)))}
            ),
            @ApiResponse(responseCode = "404", description = "No prescriptions are stored in the database"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/infos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrescriptionDto>> getPrescriptions() {
        List<Prescription> prescriptionList = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptionList
                .stream()
                .map(PrescriptionMapper::prescriptionToPrescriptionDto)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get a prescription using an id",
            operationId = "getPrescription",
            description = "By using a valid id you can get the information about its corresponding prescription")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "prescription found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PrescriptionDto.class))}),
            @ApiResponse(responseCode = "404", description = "prescription not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrescriptionDto> getPrescription(@PathVariable("id") Long id) {
        PrescriptionDto prescriptionDto = PrescriptionMapper.prescriptionToPrescriptionDto(prescriptionService.getPrescriptionById(id));
        return ResponseEntity.ok(prescriptionDto);
    }

    @Operation(summary = "Create a new prescription",
            operationId = "createPrescription",
            description = "By providing the basic values for a prescription you can create one")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "prescription was created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PrescriptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPrescription(@RequestBody @Valid PrescriptionDto newPrescription) {
        Prescription toBeSavedPrescription = new Prescription(newPrescription.getMedicamentName(), newPrescription.getPrice(), newPrescription.getAmountToTake());
        Prescription savedPrescription = prescriptionService.addPrescription(toBeSavedPrescription);
        URI uri = URI.create("api/prescriptions/" + savedPrescription.getId());
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update a prescription",
            operationId = "changePrescription",
            description = "Change the information about a prescription by providing an id and new data")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "prescription was updated"),
            @ApiResponse(responseCode = "404", description = "prescription not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changePrescription(@PathVariable Long id, @RequestBody Prescription prescription) {
        Boolean isOperationSuccessful = prescriptionService.updatePrescription(id, prescription);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Patch a prescription",
            operationId = "patchPrescription",
            description = "A part of the information about prescription can be changed")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "prescription was patched"),
            @ApiResponse(responseCode = "404", description = "prescription not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> patchPrescription(@PathVariable("id") Long id, @RequestBody Prescription prescription) {
        Boolean isOperationSuccessful = prescriptionService.patchPrescription(id, prescription);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a prescription",
            operationId = "removePrescription",
            description = "This endpoint removes a prescription from the database when an id is provided")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "prescription was deleted"),
            @ApiResponse(responseCode = "404", description = "prescription not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePrescription(@PathVariable("id") Long id) {
        Boolean isOperationSuccessful = prescriptionService.deletePrescription(id);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
