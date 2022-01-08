package com.example.medrest.service;

import com.example.medrest.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    public final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
}
