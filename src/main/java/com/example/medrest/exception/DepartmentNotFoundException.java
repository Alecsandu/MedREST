package com.example.medrest.exception;

public class DepartmentNotFoundException extends NotFoundException {
    public DepartmentNotFoundException() {
        super("Department not found!");
    }
}
