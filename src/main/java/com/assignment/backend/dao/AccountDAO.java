package com.assignment.backend.dao;

import com.assignment.backend.entity.Account;
import com.assignment.backend.exceptions.AccountExistException;

import java.util.List;
import java.util.Optional;

public interface AccountDAO {

    Account create(String accountNumber, double balance) throws AccountExistException;

    Optional<Account> get(String accountNumber);

    Account update(Account account);

    void update(Account... account);

    boolean delete(String accountNumber);

    List<Account> listAccounts();

}
