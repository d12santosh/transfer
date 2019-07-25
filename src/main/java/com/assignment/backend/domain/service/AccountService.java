package com.assignment.backend.domain.service;


import com.assignment.backend.domain.entities.Account;
import com.assignment.backend.rest.request.AccountRequest;
import com.assignment.backend.rest.request.CreateAccountRequest;
import com.assignment.backend.rest.request.TransferRequest;
import com.assignment.backend.rest.response.AccountInfoResponse;

import java.util.List;

public interface AccountService {

    Account getAccount(String accountNumber);

    List<AccountInfoResponse> getAllAccounts();

    String transfer(TransferRequest request);

    String createAccount(CreateAccountRequest request);

    String withdrawAmountFromAccount(AccountRequest request);

    String depositAmountToAccount(AccountRequest request);
}
