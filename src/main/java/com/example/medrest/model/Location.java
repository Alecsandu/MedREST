package com.example.medrest.model;

import com.example.medrest.dto.LocationDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private Integer specialNumber;

    //am sters relatia de one to one de aici cu department pentru a nu fi bidirectionala

    public Location() {
        // Every entity has a default constructor declared
    }

    public Location(String city, String street, Integer number) {
        this.city = city;
        this.street = street;
        this.specialNumber = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setSpecialNumber(Integer number) {
        this.specialNumber = number;
    }

    public void patch (Location location) {
        if (location != null) {
            if (location.getCity() != null) {
                city = location.getCity();
            }
            if (location.getStreet() != null) {
                street = location.getStreet();
            }
            if (location.getSpecialNumber() != null) {
                specialNumber = location.getSpecialNumber();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location location = (Location) obj;
        return  Objects.equals(city, location.city) &&
                Objects.equals(street, location.street) &&
                Objects.equals(specialNumber, location.specialNumber);
    }
}
