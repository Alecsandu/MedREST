package com.example.medrest.service;

import com.example.medrest.exception.DepartmentNotFoundException;
import com.example.medrest.model.Department;
import com.example.medrest.repository.DepartmentRepository;
import com.example.medrest.repository.DoctorRepository;
import com.example.medrest.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final LocationRepository locationRepository;
    private final DoctorRepository doctorRepository;

    public DepartmentService(@Autowired DepartmentRepository departmentRepository,@Autowired LocationRepository locationRepository,@Autowired DoctorRepository doctorRepository) {
        this.departmentRepository = departmentRepository;
        this.locationRepository = locationRepository;
        this.doctorRepository = doctorRepository;
    }

    public Collection<Department> getDepartments() {
        return (Collection<Department>) departmentRepository.findAll();
    }

    public Department getDepartment(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if(department.isPresent()) {
            return department.get();
        } else {
            throw new DepartmentNotFoundException();
        }
    }
}
