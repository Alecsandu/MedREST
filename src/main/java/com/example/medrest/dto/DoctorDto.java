package com.example.medrest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DoctorDto {
    @NotNull
    @NotBlank
    private String name;

    private Integer salary;

    public DoctorDto() {
        // Every entity needs a default constructor declared
    }

    public DoctorDto(String name, Integer salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
