package com.assignment.backend.dao.impl;

import com.assignment.backend.dao.AccountDAO;
import com.assignment.backend.entity.Account;
import com.assignment.backend.exceptions.AccountDoesNotExistException;
import com.assignment.backend.exceptions.AccountExistException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class SavingsAccountDAOImpl implements AccountDAO {

    private final ConcurrentMap<String, Account> accountMap;

    public SavingsAccountDAOImpl() {
        this.accountMap = new ConcurrentHashMap<>();
    }

    /**
     * @param accountNumber account number to create new account
     * @param balance       initial balance of account
     * @return newly created account
     * @throws AccountExistException if account exists
     */
    @Override
    public Account create(String accountNumber, double balance) {

        Account account = new Account(accountNumber, LocalDate.now(), balance);

        if (null != accountMap.putIfAbsent(account.getAccountNumber(), account)) {
            throw new AccountExistException(String.format("Account with number %s already exist", accountNumber));
        }
        return account;
    }

    @Override
    public Optional<Account> get(String accountNumber) {
        return Optional.ofNullable(accountMap.get(accountNumber));

    }

    @Override
    public Account update(Account account) {
        // This means no account existed so update failed. return null
        if (null == accountMap.replace(account.getAccountNumber(), account)) {
            throw new AccountDoesNotExistException(String.format("Account with number %s does not exist", account.getAccountNumber()));
        }
        // Update succeeded return the account
        return account;
    }

    @Override
    public boolean delete(String accountNumber) {
        return null != accountMap.remove(accountNumber);

    }

    @Override
    public Account transfer(Account from, Account to, double amount) {


        return null;
    }

    @Override
    public List<Account> listAccounts() {
        return accountMap.values()
                .stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }
}
