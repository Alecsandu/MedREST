package com.example.medrest.controller;

import com.example.medrest.dto.DepartmentDto;
import com.example.medrest.exception.CanNotDeleteException;
import com.example.medrest.mapper.DepartmentMapper;
import com.example.medrest.model.Department;
import com.example.medrest.model.Location;
import com.example.medrest.service.DepartmentService;
import com.example.medrest.service.DoctorService;
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
@RequestMapping("api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final LocationService locationService;
    private final DoctorService doctorService;

    public DepartmentController(@Autowired DepartmentService departmentService,
                                @Autowired LocationService locationService,
                                @Autowired DoctorService doctorService) {
        this.departmentService = departmentService;
        this.locationService = locationService;
        this.doctorService = doctorService;
    }

    @Operation(summary = "Get the names of all the departments",
            operationId = "getAllDepartmentNames",
            description = "Simple select which provides us with all the department names")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departments were found",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = DepartmentDto.class)))}
            ),
            @ApiResponse(responseCode = "404", description = "No departments in the database")
    })
    @GetMapping("/names")
    public ResponseEntity<List<DepartmentDto>> getAllDepartmentNames() {
        List<DepartmentDto> departmentDtoList = departmentService.getAllDepartments().stream().map(DepartmentMapper::departmentToDepartmentDto).collect(Collectors.toList());
        return ResponseEntity.ok(departmentDtoList);
    }

    @Operation(summary = "Get department by id",
            operationId = "getDepartmentById",
            description = "With the help of a department id we can get information about it")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found department",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DepartmentDto.class))}),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("id") Long id) {
        Department department = departmentService.getDepartment(id);
        return ResponseEntity.ok(DepartmentMapper.departmentToDepartmentDto(department));
    }

    @Operation(summary = "Create a new department",
            operationId = "createDepartment",
            description = "By providing the basic values for a department you can create one")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "department was created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DepartmentDto.class))}),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createDepartment(@RequestBody @Valid DepartmentDto newDepartment) {
        Department toBeSavedDepartment = new Department(newDepartment.getName());
        Department savedDepartment = departmentService.addDepartment(toBeSavedDepartment);
        URI uri = URI.create("api/departments/" + savedDepartment.getId());
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update a department",
            operationId = "changeDepartment",
            description = "Change the information about a department by providing an id and new data")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Department was updated"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeDepartment(@PathVariable Long id, @RequestBody @Valid Department departmentEntity) {
        Boolean isOperationSuccessful = departmentService.updateDepartment(id, departmentEntity);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Patch a department",
            operationId = "patchDepartment",
            description = "A part of the information about department can be changed")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Department was patched"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> patchDepartment(@PathVariable("id") Long id, @RequestBody Department departmentEntity) {
        Boolean isOperationSuccessful = departmentService.patchDepartment(id, departmentEntity);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a department",
            operationId = "removeDepartment",
            description = "This endpoint removes a department from the database when an id is provided")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Department was deleted"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "409", description = "The department can not be deleted because it has doctors assigned to it"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDepartment(@PathVariable("id") Long id) {
        Boolean canDepartmentBeDeleted = doctorService.checkIfAnyDoctorIsAssignedToGivenDepartment(id);
        if (!canDepartmentBeDeleted) {
            throw new CanNotDeleteException("Department can not be deleted");
        }
        Boolean isOperationSuccessful = departmentService.deleteDepartment(id);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Set a location for a department",
            operationId = "setDepartmentLocation",
            description = "Firstly give the id of the department that you want to modify and" +
                    " secondly the id of the location that you want your department to be set in")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Location for the department was set"),
            @ApiResponse(responseCode = "404", description = "Department or location not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PatchMapping(path = "/{depId}/location/{locId}")
    public ResponseEntity<Void> setDepartmentLocation(@PathVariable("depId") Long departmentId, @PathVariable("locId") Long locationId) {
        Location existingLocation = locationService.getLocationById(locationId);
        Department existingDepartment = departmentService.getDepartment(departmentId);
        existingDepartment.setLocation(existingLocation);
        Boolean isOperationSuccessful = departmentService.patchDepartment(departmentId, existingDepartment);
        if (isOperationSuccessful) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
