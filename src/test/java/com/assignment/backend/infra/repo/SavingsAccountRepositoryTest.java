package com.assignment.backend.infra.repo;

import com.assignment.backend.application.exceptions.AccountDoesNotExistException;
import com.assignment.backend.application.exceptions.AccountExistException;
import com.assignment.backend.application.exceptions.InsufficientBalanceException;
import com.assignment.backend.domain.entities.Account;
import com.assignment.backend.domain.repo.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.assignment.backend.TestFixture.*;
import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountRepositoryTest {

    private AccountRepository daoToTest;
    private Account dummyAccount;

    @BeforeEach
    void setUp() {
        daoToTest = new SavingsAccountRepository();
        dummyAccount = daoToTest.create(getDummyAccount());
    }


    @Test
    void test_create_Happy_Path() {
        assertNotNull(dummyAccount);
        assertEquals(DUMMY_ACCOUNT_NUMBER, dummyAccount.getAccountNumber(), MSG_FOR_ACCOUNT_NUMBER);
        assertEquals(DUMMY_BALANCE, dummyAccount.getBalance());
    }

    @Test
    void test_create_To_Throw_Account_exist_Exception() {
        assertThrows(AccountExistException.class, () -> daoToTest.create(getDummyAccount()));
    }

    @Test
    void test_get__Happy_Path() {
        Optional<Account> actual = daoToTest.get(DUMMY_ACCOUNT_NUMBER);
        assertTrue(actual.isPresent());
        assertEquals(actual.get().getAccountNumber(), DUMMY_ACCOUNT_NUMBER);
    }

    @Test
    void test_get_with_Invalid_account_Number() {
        Optional<Account> actual = daoToTest.get(DUMMY_ACCOUNT_NUMBER_1);
        assertFalse(actual.isPresent());
    }

    @Test
    void test_update_Happy_Path() throws InterruptedException {
        dummyAccount.withdraw(5d);
        Account actual = daoToTest.update(dummyAccount);
        assertEquals(5d, actual.getBalance());
    }

    @Test
    void test_Update_For_Condition_Await_For_Withdrawing_And_Depositing_Amount() {
        Thread withdraw = new Thread(() -> {
            try {
                dummyAccount.withdraw(25d);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread deposit = new Thread(() -> dummyAccount.deposit(25d));

        withdraw.start();
        deposit.start();
        Account actual = daoToTest.update(dummyAccount);
        assertEquals(actual.getBalance(), 10d);
    }

    @Test
    void test_Update_By_Withdrawing_Amount_Greater_Than_Balance() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Account> future = executor.submit(() -> {
            System.out.println("** Started");
            dummyAccount.withdraw(25d);
            return dummyAccount;
        });

        ExecutionException executionException = assertThrows(ExecutionException.class, future::get);// raises ExecutionException for any uncaught exception in child
        assertEquals(executionException.getCause().getClass().getSimpleName(), InsufficientBalanceException.class.getSimpleName());
        executor.shutdown();
        System.out.println("** stopped");
    }

    @Test
    void test_update_With_invalid_account_number() {
        Account account = getDummyAccount1();
        assertThrows(AccountDoesNotExistException.class, () -> daoToTest.update(account));
    }

    @Test
    void delete_Happy_Path() {
        daoToTest.delete(dummyAccount.getAccountNumber());
        Optional<Account> account1 = daoToTest.get(DUMMY_ACCOUNT_NUMBER);
        assertFalse(account1.isPresent());
    }

    @Test
    void test_Delete_With_Invalid_Account_Number() {
        assertThrows(AccountDoesNotExistException.class, () -> daoToTest.delete(DUMMY_ACCOUNT_NUMBER_1));
    }

    @Test
    void listAccounts_Happy_Path() {
        assertEquals(1, daoToTest.listAccounts().size());
    }

    @Test
    void getAll() {
        assertEquals(1, daoToTest.getAll().size());
    }
}