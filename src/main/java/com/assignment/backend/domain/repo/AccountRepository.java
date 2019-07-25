package com.assignment.backend.domain.repo;

import com.assignment.backend.application.exceptions.AccountExistException;
import com.assignment.backend.domain.entities.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account create(Account account) throws AccountExistException;

    Optional<Account> get(String accountNumber);

    List<Account> getAll();

    Account update(Account account);

    void update(Account... account);

    boolean delete(String accountNumber);

    List<Account> listAccounts();

}
