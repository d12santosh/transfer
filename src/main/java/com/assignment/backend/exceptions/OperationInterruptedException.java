package com.assignment.backend.exceptions;

public class OperationInterruptedException extends RuntimeException {

    public OperationInterruptedException(String message) {
        super(message);
    }
}
