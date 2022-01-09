package com.example.medrest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "department")
    @JsonBackReference
    private List<Doctor> doctors;

    public Department() {
        // Every entity has a default constructor declared
    }

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String name) {
        this.departmentName = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void patch (Department department) {
        if (department != null) {
            if (department.getDepartmentName() != null) {
                departmentName = department.getDepartmentName();
            }
        }
    }
}
