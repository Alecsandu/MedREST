package com.example.medrest.service;

import com.example.medrest.exception.DepartmentNotFoundException;
import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Department;
import com.example.medrest.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(@Autowired DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
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

    public Boolean checkIfAnyDepartmentHasGivenLocation(Long id) {
        List<Department> existingDepartments = departmentRepository.findAll();
        for(Department department:existingDepartments) {
            if (Objects.equals(department.getLocation().getId(), id)) {
                return false;
            }
        }
        return true;
    }
}
