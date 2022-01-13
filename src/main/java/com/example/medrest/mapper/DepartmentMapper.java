package com.example.medrest.mapper;

import com.example.medrest.dto.DepartmentDto;
import com.example.medrest.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public static DepartmentDto departmentToDepartmentDto(Department department) {
        return new DepartmentDto(department.getDepartmentName());
    }
}
