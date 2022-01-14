package com.example.medrest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SpecialisationDto {
    @NotNull
    @NotBlank
    private String name;

    private Integer minSalary;

    private Integer maxSalary;

    public SpecialisationDto() {
    }

    public SpecialisationDto(String name, Integer minSalary, Integer maxSalary) {
        this.name = name;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public Integer getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Integer maxSalary) {
        this.maxSalary = maxSalary;
    }
}
