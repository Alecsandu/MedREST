package com.example.medrest.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "salary")
    private Integer salary;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialisation specialization;

    @ManyToMany(mappedBy = "doctors")
    private List<Patient> patients;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    @JsonManagedReference
    private Department department;

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
