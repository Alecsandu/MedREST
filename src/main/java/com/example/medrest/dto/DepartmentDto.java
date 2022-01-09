package com.example.medrest.dto;

public class DepartmentDto {
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
