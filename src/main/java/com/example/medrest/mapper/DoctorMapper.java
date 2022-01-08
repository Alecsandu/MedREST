package com.example.medrest.mapper;

import com.example.medrest.dto.DoctorDto;
import com.example.medrest.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {
    public DoctorDto doctorToDoctorDto(Doctor doctor) {
        return new DoctorDto(doctor.getName(), doctor.getSalary());
    }
}
