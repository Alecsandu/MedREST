package com.example.medrest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "department_name", nullable = false)
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Department department = (Department) obj;
        return Objects.equals(id, department.getId()) &&
                Objects.equals(departmentName, department.getDepartmentName());
    }
}
