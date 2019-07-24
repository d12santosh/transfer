package com.assignment.backend;

import com.assignment.backend.rest.AccountRequest;
import com.assignment.backend.rest.TransferRequest;

public class TestFixture {

    protected static final double DUMMY_BALANCE = 10d;
    protected static final String DUMMY_ACCOUNT_NUMBER = "accountNumber";
    protected static final String DUMMY_ACCOUNT_NUMBER_1 = "accountNumber1";
    protected static final String MSG_FOR_ACCOUNT_NUMBER = "The created Account Number";

    protected static TransferRequest createTransferRequest(String fromAccount, String toAccount, double amount) {
        return new TransferRequest(fromAccount, toAccount, amount);
    }

    protected static AccountRequest createAccountRequest(String accountNumber, double amount) {
        return new AccountRequest(accountNumber,  amount);
    }
}
