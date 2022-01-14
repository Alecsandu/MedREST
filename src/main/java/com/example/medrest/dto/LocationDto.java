package com.example.medrest.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LocationDto {
    @NotNull
    @NotBlank
    private String city;

    @NotNull
    @NotBlank
    private String street;

    @Min(1)
    @Max(10000)
    private Integer specialNumber;

    public LocationDto() {
        // Every entity has a default constructor declared
    }

    public LocationDto(String city, String street, Integer specialNumber) {
        this.city = city;
        this.street = street;
        this.specialNumber = specialNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getSpecialNumber() {
        return specialNumber;
    }

    public void setSpecialNumber(Integer specialNumber) {
        this.specialNumber = specialNumber;
    }
}
