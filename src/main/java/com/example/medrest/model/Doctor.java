package com.example.medrest.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialisation specialization;

    @ManyToMany(mappedBy = "doctors")
    private List<Patient> patients;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    @JsonManagedReference
    private Department department;

    @Column(name = "salary")
    private Integer salary;

    public Doctor() {
        // Every entity has a default constructor declared
    }

    public Doctor(String name, Integer salary) {
        this.name = name;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Specialisation getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialisation specialization) {
        this.specialization = specialization;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void patch (Doctor doctor) {
        if (doctor != null) {
            if (doctor.getName() != null) {
                name = doctor.getName();
            }
            if(doctor.getSalary() != null) {
                salary = doctor.getSalary();
            }
        }
    }
}
