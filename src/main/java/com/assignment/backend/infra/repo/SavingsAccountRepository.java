package com.assignment.backend.infra.repo;

import com.assignment.backend.application.exceptions.AccountDoesNotExistException;
import com.assignment.backend.application.exceptions.AccountExistException;
import com.assignment.backend.domain.entities.Account;
import com.assignment.backend.domain.repo.AccountRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.assignment.backend.util.Constants.*;

@Slf4j
@Singleton
public class SavingsAccountRepository implements AccountRepository {

    private ConcurrentMap<String, Account> accountMap;

    @Inject
    public SavingsAccountRepository() {
        log.error("SavingsAccountDAOImpl --> Constructor is called");
        this.accountMap = new ConcurrentHashMap<>();
    }

    /**
     * @param account account to create
     * @return newly created account
     * @throws AccountExistException if account exists
     */
    @Override
    public Account create(Account account) {

        if (null != accountMap.putIfAbsent(account.getAccountNumber(), account)) {
            String msg = String.format(DEFAULT_AEE_MSG_FMT, account.getAccountNumber());
            log.warn(msg);
            throw new AccountExistException(msg);
        }
        log.info("created account with number {} with balance {}", new Object[]{account.getAccountNumber(), account.getBalance()});
        return account;
    }

    @Override
    public Optional<Account> get(String accountNumber) {
        return Optional.ofNullable(accountMap.get(accountNumber));

    }

    @Override
    public List<Account> getAll() {
        return new ArrayList<>(accountMap.values());
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
