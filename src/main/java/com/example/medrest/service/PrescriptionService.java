package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Prescription;
import com.example.medrest.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {
    public final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = (List<Prescription>) prescriptionRepository.findAll();
        if (!prescriptions.isEmpty()) {
            return prescriptions;
        } else {
            throw new NotFoundException("No prescription was found in the database");
        }
    }

    public Prescription getPrescriptionById(Long id) {
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        if (prescription.isPresent()) {
            return prescription.get();
        } else {
            throw new NotFoundException("Prescription not found");
        }
    }

    public Prescription addPrescription(Prescription prescription) {
        if (prescription != null) {
            return prescriptionRepository.save(prescription);
        } else {
            throw new RuntimeException("The given prescription is null");
        }
    }

    public Boolean updatePrescription(Long id, Prescription prescription) {
        Optional<Prescription> existingPrescription = prescriptionRepository.findById(id);
        if (existingPrescription.isPresent()) {
            prescription.setId(id);
            prescriptionRepository.save(prescription);
            return true;
        } else {
            return false;
        }
    }

    public Boolean patchPrescription(Long id, Prescription prescription) {
        Optional<Prescription> existingPrescription = prescriptionRepository.findById(id);
        if (existingPrescription.isPresent()) {
            existingPrescription.get().patch(prescription);
            prescriptionRepository.save(existingPrescription.get());
            return true;
        } else {
            return false;
        }
    }

    public Boolean deletePrescription(Long id) {
        Optional<Prescription> prescription = prescriptionRepository.findById(id);
        if (prescription.isPresent()) {
            prescriptionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
