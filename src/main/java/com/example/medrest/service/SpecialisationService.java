package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Specialisation;
import com.example.medrest.repository.SpecialisationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialisationService {
    public final SpecialisationRepository specialisationRepository;

    public SpecialisationService(SpecialisationRepository specialisationRepository) {
        this.specialisationRepository = specialisationRepository;
    }

    public List<Specialisation> getSpecializations() {
        List<Specialisation> specialisations = specialisationRepository.findAll();
        if(!specialisations.isEmpty()) {
            return specialisations;
        } else {
            throw new NotFoundException("No specialization was found in the database!");
        }
    }

    public Specialisation getSpecialisationById(Long id) {
        Optional<Specialisation> specialisation = specialisationRepository.findById(id);
        if(specialisation.isPresent()) {
            return specialisation.get();
        } else {
            throw new NotFoundException("Specialisation not found");
        }
    }

    public Specialisation addSpecialisation(Specialisation specialisation) {
        if (specialisation != null) {
            return specialisationRepository.save(specialisation);
        } else {
            throw new RuntimeException("The given specialisation is null");
        }
    }

    public Boolean updateSpecialisation(Long id, Specialisation specialisation) {
        Optional<Specialisation> existingSpecialisation = specialisationRepository.findById(id);
        if (existingSpecialisation.isPresent()) {
            specialisation.setId(id);
            specialisationRepository.save(specialisation);
            return true;
        } else {
            return false;
        }
    }

    public Boolean patchSpecialisation(Long id, Specialisation specialisation) {
        Optional<Specialisation> existingSpecialisation = specialisationRepository.findById(id);
        if (existingSpecialisation.isPresent()) {
            existingSpecialisation.get().patch(specialisation);
            specialisationRepository.save(existingSpecialisation.get());
            return true;
        } else {
            return false;
        }
    }

    public Boolean deleteSpecialisation(Long id) {
        Optional<Specialisation> specialisation = specialisationRepository.findById(id);
        if (specialisation.isPresent()) {
            specialisationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
