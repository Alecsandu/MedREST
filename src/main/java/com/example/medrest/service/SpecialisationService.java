package com.example.medrest.service;

import com.example.medrest.repository.SpecialisationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SpecialisationService {
    public final SpecialisationRepository specialisationRepository;

    public SpecialisationService(SpecialisationRepository specialisationRepository) {
        this.specialisationRepository = specialisationRepository;
    }
}
