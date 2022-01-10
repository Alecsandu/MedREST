package com.example.medrest.service;

import com.example.medrest.exception.NotFoundException;
import com.example.medrest.model.Department;
import com.example.medrest.repository.DepartmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private DepartmentService departmentService;

    @Test
    @DisplayName("Bad path when we throw exception because we don't have any departments in the database")
    void getAllDepartmentsEmptyListFlow() {
        List<Department> departments = new ArrayList<>();

        when(departmentRepository.findAll()).thenReturn(departments);
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> departmentService.getAllDepartments());

        assertNotNull(exception);
        assertEquals("No departments in the database!", exception.getMessage());
    }

    @Test
    @DisplayName("Good path, when we have departments and we return a list with all of them")
    void getAllDepartmentsListWithItemsFlow() {
        Department department1 = new Department();
        Department department2 = new Department();
        List<Department> departments = new ArrayList<>();
        departments.add(department1);
        departments.add(department2);

        when(departmentRepository.findAll()).thenReturn(departments);
        List<Department> result = departmentService.getAllDepartments();

        assertNotNull(result);
        assertEquals(departments, result);
    }

    @Test
    @DisplayName("Good path when the department with the given id exists")
    void getDepartmentIsPresentTruePath() {
        //arrange
        Department department = new Department();
        when(departmentRepository.findById(any()))
                .thenReturn(Optional.of(department));

        //act
        Department result = departmentService.getDepartment(anyLong());

        //assert
        assertNotNull(result);
        assertEquals(department, result);
    }

    @Test
    @DisplayName("Bad path when the department with the given id doesn't exist")
    void getDepartmentIsPresentFalsePath() {
        //arrange
        when(departmentRepository.findById(any()))
                .thenReturn(Optional.empty());

        //act
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> departmentService.getDepartment(anyLong()));

        //assert
        assertEquals("The department with the given id was not found!",
                exception.getMessage());
    }

    @Test
    @DisplayName("Good flow when the given department is not null")
    void addDepartmentGoodFlow() {
        Department department = new Department("Centru");
        Department testDepartment = new Department("Centru");
        testDepartment.setId(1L);

        when(departmentRepository.save(any(Department.class))).thenReturn(testDepartment);
        Department result = departmentService.addDepartment(department);

        assertNotNull(result);
        assertEquals(testDepartment.getId(), result.getId());
        assertEquals(testDepartment.getDepartmentName(), result.getDepartmentName());
    }

    @Test
    @DisplayName("Bad path when the department that we want to update does not exist")
    void updateDepartmentWhenTheDepartmentWithGivenIdDoesNotExist() {
        Department department = new Department();

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Boolean result = departmentService.updateDepartment(anyLong(), department);

        assertNotNull(result);
        assertFalse(result);
    }

    @Test
    @DisplayName("Good path when the patch was done")
    void patchDepartmentWhenThePatchIsSuccessful() {
        Department department = new Department("Centru");
        Department patchedDepartment = new Department("Vest");  //the final result

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(patchedDepartment);
        Boolean result = departmentService.patchDepartment(anyLong(), patchedDepartment);

        assertTrue(result);
    }

    @Test
    @DisplayName("Bad path when we don't have a department with the given id")
    void deleteDepartmentWhenTheIdDoesNotReturnAnyDepartment() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Boolean result = departmentService.deleteDepartment(anyLong());

        assertNotNull(result);
        assertFalse(result);
    }
}