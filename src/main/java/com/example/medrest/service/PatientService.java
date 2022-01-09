package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Patient;
import com.example.medrest.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    public final PatientRepository patientRepository;

    public PatientService(@Autowired PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = (List<Patient>) patientRepository.findAll();
        if(!patients.isEmpty()) {
            return patients;
        } else {
            throw new NotFoundException("No patients found!");
        }
    }

    public Patient getPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            return patient.get();
        } else {
            throw new NotFoundException("No patient with the given id was found!");
        }
    }

    public Patient addPatient(Patient patient) {
        if (patient != null) {
            return patientRepository.save(patient);
        } else {
            throw new RuntimeException("The given patient doesn't contain any data!");
        }
    }

    public Boolean updatePatient(Long id, Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findById(id);
        if (existingPatient.isPresent()) {
            patient.setId(id);
            patientRepository.save(patient);
            return true;
        } else {
            return false;
        }
    }

    public Boolean patchPatient(Long id, Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findById(id);
        if (existingPatient.isPresent()) {
            existingPatient.get().patch(patient);
            patientRepository.save(existingPatient.get());
            return true;
        } else {
            return false;
        }
    }

    public Boolean deletePatient(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            patientRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
