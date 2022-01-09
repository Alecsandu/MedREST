package com.example.medrest.dto;

public class LocationDto {
    private String city;

    private String street;

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
