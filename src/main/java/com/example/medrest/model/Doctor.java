package com.example.medrest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "salary")
    private Integer salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    @JsonIgnore
    private Specialisation specialization;

    @ManyToMany(mappedBy = "doctors", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Patient> patients;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    @JsonIgnore
    private Department department;

    public Doctor() {
        // Every entity has a default constructor declared
    }

    public Doctor(String name, Integer salary) {
        this.name = name;
        this.salary = salary;
        this.patients = new HashSet<>();
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

    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void addPatients(Patient patient) {
        patients.add(patient);
    }

    public void removePatient(Patient patient) {
        patients.remove(patient);
    }

    public void patch (Doctor doctor) {
        if (doctor != null) {
            if (doctor.getName() != null) {
                name = doctor.getName();
            }
            if (doctor.getSalary() != null) {
                salary = doctor.getSalary();
            }
            if (doctor.getSpecialization() != null) {
                specialization = doctor.getSpecialization();
            }
            if (doctor.getDepartment() != null) {
                department = doctor.getDepartment();
            }
            if (doctor.getPatients() != null && !doctor.getPatients().isEmpty()) {
                patients = doctor.getPatients();
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
        Doctor doctor = (Doctor) obj;
        return Objects.equals(id, doctor.getId()) &&
                Objects.equals(name, doctor.getName()) &&
                Objects.equals(salary, doctor.getSalary());
    }
}
