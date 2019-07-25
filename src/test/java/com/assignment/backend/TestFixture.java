package com.assignment.backend;

import com.assignment.backend.domain.entities.Account;
import com.assignment.backend.rest.request.AccountRequest;
import com.assignment.backend.rest.request.CreateAccountRequest;
import com.assignment.backend.rest.request.TransferRequest;

import java.time.LocalDate;

public class TestFixture {

    public static final double DUMMY_BALANCE = 10d;
    public static final String DUMMY_ACCOUNT_NUMBER = "accountNumber";
    public static final String DUMMY_ACCOUNT_NUMBER_1 = "accountNumber1";
    public static final String MSG_FOR_ACCOUNT_NUMBER = "The created Account Number";
    public static final String DUMMY_FIRST_NAME = "firstName";
    private static final String DUMMY_FIRST_NAME_1 = "firstName1";
    public static final String DUMMY_LAST_NAME = "lastName";
    private static final String DUMMY_LAST_NAME_1 = "lastName1";


    public static TransferRequest createTransferRequest(String fromAccount, String toAccount, double amount) {
        return new TransferRequest(fromAccount, toAccount, amount);
    }

    public static AccountRequest createAccountRequest(String accountNumber, double amount) {
        return new AccountRequest(accountNumber,  amount);
    }

    public static CreateAccountRequest getCreateAccountRequest(String accountNumber, double dummyBalance, String firstName, String lastName) {
        return new CreateAccountRequest(accountNumber, dummyBalance, firstName, lastName);
    }

    public static Account getDummyAccount() {
        return new Account(DUMMY_ACCOUNT_NUMBER, LocalDate.now(), DUMMY_BALANCE, DUMMY_FIRST_NAME, DUMMY_LAST_NAME);
    }

    public static Account getDummyAccount1() {
        return new Account(DUMMY_ACCOUNT_NUMBER_1, LocalDate.now(), DUMMY_BALANCE, DUMMY_FIRST_NAME_1, DUMMY_LAST_NAME_1);
    }
}
