package com.assignment.backend.domain.service.impl;


import com.assignment.backend.application.exceptions.AccountDoesNotExistException;
import com.assignment.backend.application.exceptions.OperationInterruptedException;
import com.assignment.backend.application.mappers.AccountMapper;
import com.assignment.backend.domain.entities.Account;
import com.assignment.backend.domain.repo.AccountRepository;
import com.assignment.backend.domain.service.AccountService;
import com.assignment.backend.rest.request.AccountRequest;
import com.assignment.backend.rest.request.CreateAccountRequest;
import com.assignment.backend.rest.request.TransferRequest;
import com.assignment.backend.rest.response.AccountInfoResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

import static com.assignment.backend.util.Constants.*;

@Singleton
public class SavingsAccountServiceImpl implements AccountService {

    private AccountRepository savingsAccountDao;

    @Inject
    public SavingsAccountServiceImpl(AccountRepository savingsAccountDao) {
        this.savingsAccountDao = savingsAccountDao;
    }

    public SavingsAccountServiceImpl(){

    }

    @Override
    public Account getAccount(String accountNumber) {
        return savingsAccountDao.get(accountNumber)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format(DEFAULT_ANFE_MSG_FMT, accountNumber)));
    }

    @Override
    public List<AccountInfoResponse> getAllAccounts() {
        return savingsAccountDao.getAll().stream().map(a -> new AccountInfoResponse(a.getAccountNumber(), a.getFirstName(), a.getLastName(), a.getBalance())).collect(Collectors.toList());
    }

    @Override
    public String transfer(TransferRequest request) {
        try {
            Account fromAccount = getAccount(request.getFromAccount());
            fromAccount.withdraw(request.getAmount());
            Account toAccount = getAccount(request.getToAccount());
            toAccount.deposit(request.getAmount());
            savingsAccountDao.update(fromAccount, toAccount);
            return String.format(DEFAULT_TRANSFER_MSG_FMT, request.getAmount(), request.getToAccount(), request.getFromAccount());
        } catch (InterruptedException e) {
            throw new OperationInterruptedException(String.format(DEFAULT_OIE_MSG_FMT, request.getFromAccount()));
        }
    }

    @Override
    public String createAccount(CreateAccountRequest request) {
        Account account = savingsAccountDao.create(AccountMapper.fromCreateAccountRequest(request));
        return String.format(DEFAULT_CREATION_MSG_FMT, account.getAccountNumber());
    }

    @Override
    public String withdrawAmountFromAccount(AccountRequest request) {
        try {
            Account account = getAccount(request.getAccountNumber());
            account.withdraw(request.getAmount());
            savingsAccountDao.update(account);
            return String.format(DEFAULT_WITHDRAW_MSG_FMT, request.getAmount(), request.getAccountNumber(), account.getBalance());
        } catch (InterruptedException e) {
            throw new OperationInterruptedException(String.format(DEFAULT_OIE_MSG_FMT, request.getAccountNumber()));
        }
    }

    @Override
    public String depositAmountToAccount(AccountRequest request) {
        Account account = getAccount(request.getAccountNumber());
        account.deposit(request.getAmount());
        savingsAccountDao.update(account);
        return String.format(DEFAULT_DEPOSIT_MSG_FMT, request.getAmount(), request.getAccountNumber(), account.getBalance());
    }
}
