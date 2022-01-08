package com.example.medrest.dto;

public class DoctorDto {
    private String name;

    private Integer salary;

    public DoctorDto() {
        // Every entity has a default constructor declared
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
