package com.example.project.exception;

public class DeveloperIsNotUser extends RuntimeException {
    public DeveloperIsNotUser(String message) {
        super(message);
    }
}
