package com.example.medrest.service;

import com.example.medrest.repository.DepartmentRepository;
import com.example.medrest.repository.DoctorRepository;
import com.example.medrest.repository.PatientRepository;
import com.example.medrest.repository.SpecialisationRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    public final DoctorRepository doctorRepository;
    public final PatientRepository patientRepository;
    public final SpecialisationRepository specialisationRepository;
    public final DepartmentRepository departmentRepository;

    public DoctorService(DoctorRepository doctorRepository, PatientRepository patientRepository, SpecialisationRepository specialisationRepository, DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.specialisationRepository = specialisationRepository;
        this.departmentRepository = departmentRepository;
    }
}
