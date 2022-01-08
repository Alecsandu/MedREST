package com.example.medrest.mapper;

import com.example.medrest.dto.SpecialisationDto;
import com.example.medrest.model.Specialisation;
import org.springframework.stereotype.Component;

@Component
public class SpecialisationMapper {
    public SpecialisationDto specialisationToSpecialisationDto(Specialisation specialisation) {
        return new SpecialisationDto(specialisation.getName(),
                specialisation.getMinSalary(),
                specialisation.getMaxSalary());
    }
}
