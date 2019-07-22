package com.assignment.backend.dao.impl;

import com.assignment.backend.dao.AccountDAO;
import com.assignment.backend.entity.Account;
import com.assignment.backend.exceptions.AccountExistException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class SavingsAccountDAOImplTest {

    private static final double DUMMY_BALANCE = 10d;
    private static final String DUMMY_ACCOUNT_NUMBER = "accountNumber";
    private static final String MSG_FOR_ACCOUNT_NUMBER = "The created Account Number";
    private static final String MSG_FOR_ACCOUNT_BALANCE = "The Initial Account Balance";

    private AccountDAO daoToTest;

    @Before
    public void setUp() {
        daoToTest = new SavingsAccountDAOImpl();
    }

    @After
    public void tearDown() {
        daoToTest = null;
    }

    @Test
    public void test_create() {
        Account account = daoToTest.create(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE);
        assertNotNull(account);
        assertEquals(MSG_FOR_ACCOUNT_NUMBER, DUMMY_ACCOUNT_NUMBER, account.getAccountNumber());
        Assert.assertEquals(MSG_FOR_ACCOUNT_BALANCE, DUMMY_BALANCE, account.getBalance(), 0);


    }

    @Test(expected = AccountExistException.class)
    public void test_create_To_Throw_Account_exist_Exception() {
        Account account = daoToTest.create(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE);
        assertNotNull(account);
        assertEquals(MSG_FOR_ACCOUNT_NUMBER, DUMMY_ACCOUNT_NUMBER, account.getAccountNumber());
        Assert.assertEquals(MSG_FOR_ACCOUNT_BALANCE, DUMMY_BALANCE, account.getBalance(), 0);
        daoToTest.create(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE);
    }

    @Test
    public void test_get_Happy_Scenario() {
        Account account = daoToTest.create(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE);
        assertNotNull(account);
        assertEquals(MSG_FOR_ACCOUNT_NUMBER, DUMMY_ACCOUNT_NUMBER, account.getAccountNumber());
        Assert.assertEquals(MSG_FOR_ACCOUNT_BALANCE, DUMMY_BALANCE, account.getBalance(), 0);
        Optional<Account> actual = daoToTest.get(DUMMY_ACCOUNT_NUMBER);
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getAccountNumber(), DUMMY_ACCOUNT_NUMBER);
    }

    @Test

    public void test_get_with_Invalid_account_Number() {
        Optional<Account> actual = daoToTest.get(DUMMY_ACCOUNT_NUMBER);
        assertFalse(actual.isPresent());
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void transfer() {
    }

    @Test
    public void listAccounts() {
    }
}