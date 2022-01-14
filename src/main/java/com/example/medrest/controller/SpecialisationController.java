package com.example.medrest.controller;

import com.example.medrest.dto.SpecialisationDto;
import com.example.medrest.exception.CanNotDeleteException;
import com.example.medrest.mapper.SpecialisationMapper;
import com.example.medrest.model.Specialisation;
import com.example.medrest.service.DoctorService;
import com.example.medrest.service.SpecialisationService;
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
@RequestMapping("api/specialisations")
public class SpecialisationController {
    private final SpecialisationService specialisationService;
    private final DoctorService doctorService;

    public SpecialisationController(@Autowired SpecialisationService specialisationService,
                                    @Autowired DoctorService doctorService) {
        this.specialisationService = specialisationService;
        this.doctorService = doctorService;
    }

    @Operation(summary = "Get information about all the specialisations",
            operationId = "getSpecialisations",
            description = "This request provides data about each specialisation entity stored in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Specialisations were found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SpecialisationDto.class)))}
            ),
            @ApiResponse(responseCode = "404", description = "No specialisations are stored in the database"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/infos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpecialisationDto>> getSpecialisations() {
        List<Specialisation> specialisationList = specialisationService.getSpecializations();
        return ResponseEntity.ok(specialisationList
                .stream()
                .map(SpecialisationMapper::specialisationToSpecialisationDto)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get specialisation using an id",
            operationId = "getSpecialisation",
            description = "By using a valid id you can get the information about its corresponding specialisation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Specialisation found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SpecialisationDto.class))}),
            @ApiResponse(responseCode = "404", description = "Specialisation not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpecialisationDto> getSpecialisation(@PathVariable("id") Long id) {
        SpecialisationDto specialisationDto = SpecialisationMapper.specialisationToSpecialisationDto(specialisationService.getSpecialisationById(id));
        return ResponseEntity.ok(specialisationDto);
    }

    @Operation(summary = "Create a new specialisation",
            operationId = "createSpecialisation",
            description = "By providing the basic values for a specialisation you can create one")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Specialisation was created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SpecialisationDto.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createSpecialisation(@RequestBody @Valid SpecialisationDto newSpecialisation) {
        Specialisation toBeSavedSpecialisation = new Specialisation(newSpecialisation.getName(), newSpecialisation.getMinSalary(), newSpecialisation.getMaxSalary());
        Specialisation savedSpecialisation = specialisationService.addSpecialisation(toBeSavedSpecialisation);
        URI uri = URI.create("api/specialisations/" + savedSpecialisation.getId());
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update a specialisation",
            operationId = "changeSpecialisation",
            description = "Change the information about a specialisation by providing an id and new data")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Specialisation was updated"),
            @ApiResponse(responseCode = "404", description = "Specialisation not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeSpecialisation(@PathVariable Long id, @RequestBody Specialisation specialisation) {
        Boolean isOperationSuccessful = specialisationService.updateSpecialisation(id, specialisation);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Patch a specialisation",
            operationId = "patchSpecialisation",
            description = "A part of the information about specialisation can be changed")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Specialisation was patched"),
            @ApiResponse(responseCode = "404", description = "Specialisation not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> patchSpecialisation(@PathVariable("id") Long id, @RequestBody Specialisation specialisation) {
        Boolean isOperationSuccessful = specialisationService.patchSpecialisation(id, specialisation);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a specialisation",
            operationId = "removeSpecialisation",
            description = "This endpoint removes a specialisation from the database when an id is provided")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Specialisation was deleted"),
            @ApiResponse(responseCode = "404", description = "Specialisation not found"),
            @ApiResponse(responseCode = "409", description = "the specialisation can not be deleted because doctors has it set"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSpecialisation(@PathVariable("id") Long id) {
        Boolean canSpecialisationBeDeleted = doctorService.checkIfAnyDoctorHasSetGivenSpecialisation(id);
        if (!canSpecialisationBeDeleted) {
            throw new CanNotDeleteException("Specialisation can not be deleted");
        }
        Boolean isOperationSuccessful = specialisationService.deleteSpecialisation(id);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
