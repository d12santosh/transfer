package com.assignment.backend.dao.impl;

import com.assignment.backend.dao.AccountDAO;
import com.assignment.backend.entity.Account;
import com.assignment.backend.exceptions.AccountDoesNotExistException;
import com.assignment.backend.exceptions.AccountExistException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.assignment.backend.util.Constants.*;

@Slf4j
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
            String msg = String.format(DEFAULT_AEE_MSG_FMT, accountNumber);
            log.warn(msg);
            throw new AccountExistException(msg);
        }
        log.info("created account with number {} with balance {}", new Object[]{accountNumber, balance});
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
            throw new AccountDoesNotExistException(String.format(DEFAULT_ANFE_MSG_FMT, account.getAccountNumber()));
        }
        // Update succeeded return the account
        return account;
    }

    @Override
    public void update(Account... accounts) {
        Arrays.stream(accounts).forEach(this::update);
    }

    @Override
    public boolean delete(String accountNumber) {
        Account removedAccount = accountMap.remove(accountNumber);
        if (null == removedAccount) {
            throw new AccountDoesNotExistException(String.format(DEFAULT_ANFE_MSG_FMT, accountNumber));
        }
        log.info(String.format(DEFAULT_DELETE_MSG_FMT, accountNumber));
        return true;
    }

    @Override
    public List<Account> listAccounts() {
        return accountMap.values()
                .stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }
}
