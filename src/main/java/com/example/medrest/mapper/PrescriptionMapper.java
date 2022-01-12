package com.example.medrest.mapper;

import com.example.medrest.dto.PrescriptionDto;
import com.example.medrest.model.Prescription;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {
    public static PrescriptionDto prescriptionToPrescriptionDto(Prescription prescription) {
        return new PrescriptionDto(prescription.getMedicamentName(),
                prescription.getPrice(),
                prescription.getAmountToTake());
    }
}
