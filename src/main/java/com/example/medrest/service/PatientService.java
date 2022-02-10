package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Patient;
import com.example.medrest.model.Prescription;
import com.example.medrest.repository.PatientRepository;
import com.example.medrest.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {
    public final PatientRepository patientRepository;
    public final PrescriptionRepository prescriptionRepository;

    public PatientService(@Autowired PatientRepository patientRepository, @Autowired PrescriptionRepository prescriptionRepository) {
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
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

    @Transactional
    public void addPrescriptionToPatient(Long prescriptionId, Long patientId) {
        Optional<Prescription> prescription = prescriptionRepository.findById(prescriptionId);
        if (prescription.isPresent()) {
            Optional<Patient> patient = patientRepository.findById(patientId);
            if (patient.isPresent()) {
                patient.get().addPrescription(prescription.get());
                patientRepository.save(patient.get());
            } else {
                throw new NotFoundException("The patient with the given id was not found!\n");
            }
        } else {
            throw new NotFoundException("The prescription with the given id was not found!\n");
        }
    }

    @Transactional
    public void removePrescriptionFromPatient(Long prescriptionId, Long patientId) {
        Optional<Prescription> prescription = prescriptionRepository.findById(prescriptionId);
        if (prescription.isPresent()) {
            Optional<Patient> patient = patientRepository.findById(patientId);
            if (patient.isPresent()) {
                patient.get().removePrescriptions(prescription.get());
                patientRepository.save(patient.get());
            } else {
                throw new NotFoundException("The patient with the given id was not found!\n");
            }
        } else {
            throw new NotFoundException("The prescription with the given id was not found!\n");
        }
    }

    @Transactional
    public List<Prescription> getPatientPrescriptionsList(Long patientId) {
         Optional<Patient> existingPatient = patientRepository.findById(patientId);
         if (existingPatient.isPresent()) {
             return new ArrayList<>(existingPatient.get().getPrescriptions());
         } else {
             throw new NotFoundException("The patient with the given id does not exist!\n");
         }
    }
}
