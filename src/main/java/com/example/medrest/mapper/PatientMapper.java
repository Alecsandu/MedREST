package com.example.medrest.mapper;

import com.example.medrest.dto.PatientDto;
import com.example.medrest.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public static PatientDto patientToPatientDto(Patient patient) {
        return new PatientDto(patient.getFirstName(),
                patient.getLastName(),
                patient.getPhoneNumber(),
                patient.getEmailAddress()
        );
    }
}
