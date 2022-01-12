package com.example.medrest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "phone_number" ,nullable = false)
    private String phoneNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @ManyToMany
    @JoinTable(
            name = "patients_doctors",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @JsonIgnore
    private List<Doctor> doctors;

    @ManyToMany
    @JoinTable(
            name = "patients_prescriptions",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "prescription_id")
    )
    @JsonIgnore
    private List<Prescription> prescriptions;

    public Patient() {
        // Every entity has a default constructor declared
    }

    public Patient(String firstName, String lastName, String phoneNumber, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void patch (Patient patient) {
        if (patient != null) {
            if (patient.getFirstName() != null) {
                firstName = patient.getFirstName();
            }
            if (patient.getLastName() != null) {
                lastName = patient.getLastName();
            }
            if (patient.getPhoneNumber() != null) {
                phoneNumber = patient.getPhoneNumber();
            }
            if (patient.getEmailAddress() != null) {
                emailAddress = patient.getEmailAddress();
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
        Patient patient = (Patient) obj;
        return Objects.equals(id, patient.getId()) &&
                Objects.equals(firstName, patient.getFirstName()) &&
                Objects.equals(lastName, patient.getLastName()) &&
                Objects.equals(phoneNumber, patient.getPhoneNumber()) &&
                Objects.equals(emailAddress, patient.getEmailAddress());
    }
}
