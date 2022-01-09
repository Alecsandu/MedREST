package com.example.medrest.service;

import com.example.medrest.exception.DepartmentNotFoundException;
import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Department;
import com.example.medrest.repository.DepartmentRepository;
import com.example.medrest.repository.DoctorRepository;
import com.example.medrest.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public List<Department> getAllDepartments() {
        List<Department> departmentList = departmentRepository.findAll();
        if (!departmentList.isEmpty()) {
            return departmentList;
        } else {
            throw new NotFoundException("No departments in the database!");
        }
    }

    public Department getDepartment(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if(department.isPresent()) {
            return department.get();
        } else {
            throw new DepartmentNotFoundException();
        }
    }

    public Department addDepartment(Department department) {
        if (department != null) {
            return departmentRepository.save(department);
        } else {
            throw new RuntimeException("The given department doesn't contain any data!");
        }
    }

    public Boolean updateDepartment(Long id, Department department) {
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if (existingDepartment.isPresent()) {
            department.setId(id);
            departmentRepository.save(department);
            return true;
        } else {
            return false;
        }
    }

    public Boolean patchDepartment(Long id, Department department) {
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if (existingDepartment.isPresent()) {
            existingDepartment.get().patch(department);
            departmentRepository.save(existingDepartment.get());
            return true;
        } else {
            return false;
        }
    }

    public Boolean deleteDepartment(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            departmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
