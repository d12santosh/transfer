package com.assignment.backend.exceptions;

public class AccountDoesNotExistException extends RuntimeException {

    public AccountDoesNotExistException(String message){
        super(message);
    }
}
