package com.example.medrest.dto;

public class DepartmentDto {
    private String name;

    public DepartmentDto() {
        // Every entity has a default constructor declared
    }

    public DepartmentDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
