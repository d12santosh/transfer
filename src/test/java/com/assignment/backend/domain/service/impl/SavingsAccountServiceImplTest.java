package com.assignment.backend.domain.service.impl;

import com.assignment.backend.application.exceptions.*;
import com.assignment.backend.domain.entities.Account;
import com.assignment.backend.domain.repo.AccountRepository;
import com.assignment.backend.domain.service.AccountService;
import com.assignment.backend.infra.repo.SavingsAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.assignment.backend.TestFixture.*;
import static org.junit.jupiter.api.Assertions.*;


class SavingsAccountServiceImplTest {

    private AccountRepository dao;
    private AccountService serviceToTest;
    private static final String DUMMY = "DUMMY";

    @BeforeEach
    void setUp() {
        dao = new SavingsAccountRepository();

        serviceToTest = new SavingsAccountServiceImpl(dao);
        dao.create(getDummyAccount());
        dao.create(getDummyAccount1());
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
        assertThrows(AccountDoesNotExistException.class, () -> serviceToTest.getAccount(DUMMY));
    }

    @Test
    void test_createAccount_Happy_Path() {

        String response = serviceToTest.createAccount(getCreateAccountRequest(DUMMY, DUMMY_BALANCE, DUMMY_FIRST_NAME, DUMMY_LAST_NAME));
        assertNotNull(response);
        assertNotNull(serviceToTest.getAccount(DUMMY));
    }


    @Test
    void test_createAccount_With_existing_account_Number() {
        assertThrows(AccountExistException.class, () -> serviceToTest.createAccount(getCreateAccountRequest(DUMMY_ACCOUNT_NUMBER_1, DUMMY_BALANCE, DUMMY_FIRST_NAME, DUMMY_LAST_NAME)));
    }

    @Test
    void test_withdrawAmountFromAccount_Happy_Path() {
        String response = serviceToTest.withdrawAmountFromAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE));
        assertNotNull(response);
        assertEquals(0, serviceToTest.getAccount(DUMMY_ACCOUNT_NUMBER).getBalance());
    }

    @Test
    void test_withdrawAmountFromAccount_InSufficient_Balance() {
        assertThrows(InsufficientBalanceException.class, () -> serviceToTest.withdrawAmountFromAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, 20d)));

    }

    @Test
    void test_withdrawAmountFromAccount_Interrupted_exception() {
        Thread.currentThread().interrupt();
        assertThrows(OperationInterruptedException.class, () -> serviceToTest.withdrawAmountFromAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, 20d)));
    }

    @Test
    void test_withdrawAmountFromAccount_InvalidOperationException_exception() {
        Thread.currentThread().interrupt();
        assertThrows(InvalidOperationException.class, () -> serviceToTest.withdrawAmountFromAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, 0d)));
    }

    @Test
    void test_depositAmountToAccount_Happy_Path() {
        String response = serviceToTest.depositAmountToAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE));
        assertNotNull(response);
        assertEquals(20d, serviceToTest.getAccount(DUMMY_ACCOUNT_NUMBER).getBalance());
    }

    @Test
    void test_depositAmountToAccount_InvalidOperationException_exception() {
        Thread.currentThread().interrupt();
        assertThrows(InvalidOperationException.class, () -> serviceToTest.depositAmountToAccount(createAccountRequest(DUMMY_ACCOUNT_NUMBER, 0d)));
    }

    @Test
    void getAllAccounts() {
        assertEquals(2, serviceToTest.getAllAccounts().size());
    }
}