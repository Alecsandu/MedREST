package com.example.medrest.controller;

import com.example.medrest.dto.DepartmentDto;
import com.example.medrest.mapper.DepartmentMapper;
import com.example.medrest.model.Department;
import com.example.medrest.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    public DepartmentController(@Autowired DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
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
        List<DepartmentDto> departmentDtoList = departmentService.getAllDepartments().stream().map(departmentMapper::departmentToDepartmentDto).collect(Collectors.toList());
        return ResponseEntity.ok(departmentDtoList);
    }

    @Operation(summary = "Get department by id",
            operationId = "getDepartmentById",
            description = "With the help of a department id we can get informations about it")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found department",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DepartmentDto.class))}),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("id") Long id) {
        Department department = departmentService.getDepartment(id);
        return ResponseEntity.ok(departmentMapper.departmentToDepartmentDto(department));
    }
}
