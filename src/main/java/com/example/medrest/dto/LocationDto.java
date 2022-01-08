package com.example.medrest.dto;

public class LocationDto {
    private String city;

    private String street;

    private Integer number;

    public LocationDto() {
        // Every entity has a default constructor declared
    }

    public LocationDto(String city, String street, Integer number) {
        this.city = city;
        this.street = street;
        this.number = number;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
