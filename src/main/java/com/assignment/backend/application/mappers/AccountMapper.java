package com.assignment.backend.application.mappers;

import com.assignment.backend.domain.entities.Account;
import com.assignment.backend.rest.request.CreateAccountRequest;

import java.time.LocalDate;

public class AccountMapper {

    public static Account fromCreateAccountRequest(CreateAccountRequest req) {
        return new Account(req.getAccountNumber(), LocalDate.now(), req.getAmount(), req.getFirstName(), req.getLastName());
    }

}
