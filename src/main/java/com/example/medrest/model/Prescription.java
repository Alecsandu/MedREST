package com.example.medrest.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long id;

    @Column(name = "medicament_name", nullable = false)
    private String medicamentName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "amount_to_take", nullable = false)
    private Integer amountToTake;

    @ManyToMany(mappedBy = "prescriptions")
    private List<Patient> patients;     //patients that have a certain prescription

    public Prescription() {
        // Every entity has a default constructor declared
    }

    public Prescription(String medicamentName, Integer price, Integer amountToTake) {
        this.medicamentName = medicamentName;
        this.price = price;
        this.amountToTake = amountToTake;
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

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
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
        }
    }
}
