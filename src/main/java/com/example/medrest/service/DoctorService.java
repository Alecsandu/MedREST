package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Doctor;
import com.example.medrest.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DoctorService {
    public final DoctorRepository doctorRepository;
    public final PatientService patientService;
    public final SpecialisationService specialisationService;
    public final DepartmentService departmentService;

    public DoctorService(@Autowired DoctorRepository doctorRepository,
                         @Autowired PatientService patientService,
                         @Autowired SpecialisationService specialisationService,
                         @Autowired DepartmentService departmentService) {
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
        this.specialisationService = specialisationService;
        this.departmentService = departmentService;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
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

    public Boolean checkIfAnyDoctorIsAssignedToGivenDepartment(Long id) {
        List<Doctor> existingDoctors = doctorRepository.findAll();
        for(Doctor doctor:existingDoctors) {
            if (Objects.equals(doctor.getDepartment().getId(), id)) {
                return false;
            }
        }
        return true;
    }

    public Boolean checkIfAnyDoctorHasSetGivenSpecialisation(Long id) {
        List<Doctor> existingDoctors = doctorRepository.findAll();
        for(Doctor doctor:existingDoctors) {
            if (Objects.equals(doctor.getSpecialization().getId(), id)) {
                return false;
            }
        }
        return true;
    }
}
