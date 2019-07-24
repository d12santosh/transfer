package com.assignment.backend.service.impl;

import com.assignment.backend.dao.AccountDAO;
import com.assignment.backend.entity.Account;
import com.assignment.backend.exceptions.AccountDoesNotExistException;
import com.assignment.backend.exceptions.OperationInterruptedException;
import com.assignment.backend.rest.AccountRequest;
import com.assignment.backend.rest.TransferRequest;
import com.assignment.backend.service.AccountService;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.assignment.backend.util.Constants.*;

@Singleton
public class SavingsAccountServiceImpl implements AccountService {

    private final AccountDAO savingsAccountDao;

    @Inject
    public SavingsAccountServiceImpl(AccountDAO savingsAccountDao) {
        this.savingsAccountDao = savingsAccountDao;
    }

    @Override
    public Account getAccount(String accountNumber) {
        return savingsAccountDao.get(accountNumber)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format(DEFAULT_ANFE_MSG_FMT, accountNumber)));
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
    public String createAccount(AccountRequest request) {
        Account account = savingsAccountDao.create(request.getAccountNumber(), request.getAmount());
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
}
