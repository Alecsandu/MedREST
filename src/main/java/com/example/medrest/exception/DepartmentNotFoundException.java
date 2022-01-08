package com.example.medrest.exception;

public class DepartmentNotFoundException extends NotFoundException {
    public DepartmentNotFoundException() {
        super("The department with the given id was not found!");
    }
}
