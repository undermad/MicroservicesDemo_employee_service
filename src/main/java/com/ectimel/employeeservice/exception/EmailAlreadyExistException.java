package com.ectimel.employeeservice.exception;

public class EmailAlreadyExistException extends RuntimeException {

    public EmailAlreadyExistException(String message) {
        super(String.format("Email %s already exist", message));
    }
}
