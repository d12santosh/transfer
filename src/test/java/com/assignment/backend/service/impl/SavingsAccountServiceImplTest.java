package com.assignment.backend.service.impl;

import com.assignment.backend.TestFixture;
import com.assignment.backend.dao.AccountDAO;
import com.assignment.backend.dao.impl.SavingsAccountDAOImpl;
import com.assignment.backend.entity.Account;
import com.assignment.backend.exceptions.AccountDoesNotExistException;
import com.assignment.backend.exceptions.AccountExistException;
import com.assignment.backend.exceptions.InsufficientBalanceException;
import com.assignment.backend.exceptions.OperationInterruptedException;
import com.assignment.backend.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountServiceImplTest extends TestFixture {

    private AccountDAO dao;
    private AccountService serviceToTest;
    private static final String DUMMY = "DUMMY";

    @BeforeEach
    void setUp() {
        dao = new SavingsAccountDAOImpl();
        serviceToTest = new SavingsAccountServiceImpl(dao);
        dao.create(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE);
        dao.create(DUMMY_ACCOUNT_NUMBER_1, DUMMY_BALANCE);
    }

    @Test
    void test_Transfer_Happy_Path() {
        String response = serviceToTest.transfer(createTransferRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_ACCOUNT_NUMBER_1, 5d));
        assertNotNull(response);
        assertTrue(dao.get(DUMMY_ACCOUNT_NUMBER).isPresent());
        assertTrue(dao.get(DUMMY_ACCOUNT_NUMBER_1).isPresent());
        assertEquals(dao.get(DUMMY_ACCOUNT_NUMBER).get().getBalance(), 5d);
        assertEquals(dao.get(DUMMY_ACCOUNT_NUMBER_1).get().getBalance(), 15d);
    }

    @Test
    void test_Transfer_For_Interrupted_Exception() {
        System.out.println("** started");
        Thread.currentThread().interrupt();
        OperationInterruptedException oIE = assertThrows(OperationInterruptedException.class, () ->
                serviceToTest.transfer(createTransferRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_ACCOUNT_NUMBER_1, 100d)));

        assertNotNull(oIE.getMessage());
        System.out.println(oIE.getMessage());
        System.out.println("** stopped");
    }

    @Test
    void test_Transfer_Invalid_From_Account_Number() {
        assertThrows(AccountDoesNotExistException.class, () -> serviceToTest.transfer(createTransferRequest(DUMMY, DUMMY_ACCOUNT_NUMBER_1, 5d)));
    }

    @Test
    void test_Transfer_Invalid_To_Account_Number() {
        assertThrows(AccountDoesNotExistException.class, () -> serviceToTest.transfer(createTransferRequest(DUMMY_ACCOUNT_NUMBER, DUMMY, 5d)));
    }

    @Test
    void getAccount_Happy_PAth() {
        Account response = serviceToTest.getAccount(DUMMY_ACCOUNT_NUMBER);
        assertNotNull(response);
        assertNotNull(response.getAccountNumber());
        assertEquals(DUMMY_ACCOUNT_NUMBER, response.getAccountNumber());
    }

    @Test
    void getAccount_With_Invalid_Account_Number() {
        assertThrows(AccountDoesNotExistException.class, () ->serviceToTest.getAccount(DUMMY));
    }

    @Test
    void test_createAccount_Happy_Path() {

        String response = serviceToTest.createAccount(createAccountRequest(DUMMY, DUMMY_BALANCE));
        assertNotNull(response);
        assertNotNull(serviceToTest.getAccount(DUMMY));
    }
    @Test
    void test_createAccount_With_existing_account_Number() {
        assertThrows(AccountExistException.class, () -> serviceToTest.createAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER_1, DUMMY_BALANCE)));
    }

    @Test
    void test_withdrawAmountFromAccount_Happy_Path() {
        String response = serviceToTest.withdrawAmountFromAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE));
        assertNotNull(response);
        assertEquals(0, serviceToTest.getAccount(DUMMY_ACCOUNT_NUMBER).getBalance());
    }

    @Test
    void test_withdrawAmountFromAccount_InSufficient_Balance() {
        assertThrows(InsufficientBalanceException.class, () ->serviceToTest.withdrawAmountFromAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, 20d)));

    }

    @Test
    void test_withdrawAmountFromAccount_Interrupted_exception() {
        Thread.currentThread().interrupt();
        assertThrows(OperationInterruptedException.class, () ->serviceToTest.withdrawAmountFromAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, 20d)));

    }
}