package com.example.medrest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @NotBlank
    @Column(name = "phone_number" ,nullable = false)
    private String phoneNumber;

    @NotBlank
    @Column(name = "email_address")
    private String emailAddress;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "patients_doctors",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @JsonIgnore
    private Set<Doctor> doctors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "patients_prescriptions",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "prescription_id")
    )
    @JsonIgnore
    private Set<Prescription> prescriptions;

    public Patient() {
        // Every entity has a default constructor declared
    }

    public Patient(String firstName, String lastName, String phoneNumber, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        prescriptions = new HashSet<>();
        doctors = new HashSet<>();
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

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Set<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void addPrescriptions(Prescription prescription) {
        prescriptions.add(prescription);
    }

    public void removePrescriptions(Prescription prescription) {
        prescriptions.remove(prescription);
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
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
            if (patient.getPrescriptions() != null && !patient.getPrescriptions().isEmpty()) {
                prescriptions = patient.getPrescriptions();
            }
            if (patient.getDoctors() != null && !patient.getDoctors().isEmpty()) {
                doctors = patient.getDoctors();
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
