package com.example.medrest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "medicament_name", nullable = false)
    private String medicamentName;

    @Column(name = "price")
    private Integer price;

    @NotNull
    @Column(name = "amount_to_take", nullable = false)
    private Integer amountToTake;

    @ManyToMany(mappedBy = "prescriptions", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Patient> patients;

    public Prescription() {
    }

    public Prescription(String medicamentName, Integer price, Integer amountToTake) {
        this.medicamentName = medicamentName;
        this.price = price;
        this.amountToTake = amountToTake;
        this.patients = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicamentName() {
        return medicamentName;
    }

    public void setMedicamentName(String medicamentName) {
        this.medicamentName = medicamentName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmountToTake() {
        return amountToTake;
    }

    public void setAmountToTake(Integer amountToTake) {
        this.amountToTake = amountToTake;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public void addPatients(Patient patient) {
        patients.add(patient);
    }

    public void removePatient(Patient patient) {
        patients.remove(patient);
    }

    public void patch (Prescription prescription) {
        if (prescription != null) {
            if (prescription.getMedicamentName() != null) {
                medicamentName = prescription.getMedicamentName();
            }
            if (prescription.getPrice() != null) {
                price = prescription.getPrice();
            }
            if (prescription.getAmountToTake() != null) {
                amountToTake = prescription.getAmountToTake();
            }
            if (prescription.getPatients() != null && !prescription.getPatients().isEmpty()) {
                patients = prescription.getPatients();
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
        Prescription prescription = (Prescription) obj;
        return Objects.equals(id, prescription.getId()) &&
                Objects.equals(medicamentName, prescription.getMedicamentName()) &&
                Objects.equals(price, prescription.getPrice()) &&
                Objects.equals(amountToTake, prescription.getAmountToTake());
    }
}
