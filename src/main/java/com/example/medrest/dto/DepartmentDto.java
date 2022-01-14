package com.example.medrest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DepartmentDto {
    @NotNull
    @NotBlank
    private String depName;

    public DepartmentDto() {
        // Every entity has a default constructor declared
    }

    public DepartmentDto(String name) {
        this.depName = name;
    }

    public String getName() {
        return depName;
    }

    public void setName(String name) {
        this.depName = name;
    }
}
