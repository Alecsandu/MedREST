package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Doctor;
import com.example.medrest.model.Location;
import com.example.medrest.repository.DepartmentRepository;
import com.example.medrest.repository.DoctorRepository;
import com.example.medrest.repository.PatientRepository;
import com.example.medrest.repository.SpecialisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    public final DoctorRepository doctorRepository;
    public final PatientRepository patientRepository;
    public final SpecialisationRepository specialisationRepository;
    public final DepartmentRepository departmentRepository;

    public DoctorService(@Autowired DoctorRepository doctorRepository,
                         @Autowired PatientRepository patientRepository,
                         @Autowired SpecialisationRepository specialisationRepository,
                         @Autowired DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.specialisationRepository = specialisationRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = (List<Doctor>) doctorRepository.findAll();
        if (!doctors.isEmpty()) {
            return doctors;
        } else {
            throw new NotFoundException("No doctors were found!");
        }
    }

    public Doctor getDoctorById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            return doctor.get();
        } else {
            throw new NotFoundException("No doctor with the given id was found!");
        }
    }

    public Doctor addDoctor(Doctor doctor) {
        if (doctor != null) {
            return doctorRepository.save(doctor);
        } else {
            throw new RuntimeException("The given doctor doesn't contain any data");
        }
    }

    public Boolean updateDoctor(Long id, Doctor doctor) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(id);
        if (existingDoctor.isPresent()) {
            doctor.setId(id);
            doctorRepository.save(doctor);
            return true;
        } else {
            return false;
        }
    }

    public Boolean patchDoctor(Long id, Doctor doctor) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(id);
        if (existingDoctor.isPresent()) {
            existingDoctor.get().patch(doctor);
            doctorRepository.save(existingDoctor.get());
            return true;
        } else {
            return false;
        }
    }

    public Boolean deleteDoctor(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            doctorRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
