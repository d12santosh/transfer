package com.assignment.backend.domain.entities;

import com.assignment.backend.application.exceptions.InvalidOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.assignment.backend.TestFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_withDraw_Happy_Path() throws InterruptedException {
        System.out.println(1);
        Account dummyAccount = getDummyAccount();
        dummyAccount.withdraw(DUMMY_BALANCE);
        assertEquals(0d, dummyAccount.getBalance());
    }

    @Test
    void test_withDraw_With_Invalid_Amount() {
        System.out.println(3);
        Account dummyAccount = getDummyAccount();
        assertThrows(InvalidOperationException.class, () -> dummyAccount.withdraw(0d));
    }

    @Test
    void test_deposit_Happy_Path() {
        System.out.println(4);
        Account dummyAccount = getDummyAccount();
        dummyAccount.deposit(DUMMY_BALANCE);
        assertEquals(20d, dummyAccount.getBalance());
    }

    @Test
    void test_deposit_With_Invalid_Amount() {
        System.out.println(5);
        Account dummyAccount = getDummyAccount();
        assertThrows(InvalidOperationException.class, () -> dummyAccount.withdraw(0d));
    }

    private Account getDummyAccount() {
        return new Account(DUMMY_ACCOUNT_NUMBER, LocalDate.now(), DUMMY_BALANCE, DUMMY_FIRST_NAME, DUMMY_LAST_NAME);
    }
}