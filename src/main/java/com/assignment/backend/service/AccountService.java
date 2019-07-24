package com.assignment.backend.service;

import com.assignment.backend.entity.Account;
import com.assignment.backend.rest.AccountRequest;
import com.assignment.backend.rest.TransferRequest;

public interface AccountService {

    Account getAccount(String accountNumber);

    String transfer(TransferRequest request);

    String createAccount(AccountRequest request);

    String withdrawAmountFromAccount(AccountRequest request);
}
