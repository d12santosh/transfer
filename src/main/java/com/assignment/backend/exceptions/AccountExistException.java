package com.assignment.backend.exceptions;

public class AccountExistException extends RuntimeException {

    public AccountExistException(String message){
        super(message);
    }
}
