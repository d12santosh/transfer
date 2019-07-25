package com.assignment.backend.domain.entities;

import com.assignment.backend.application.exceptions.InsufficientBalanceException;
import com.assignment.backend.application.exceptions.InvalidOperationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.assignment.backend.util.Constants.*;

@Data
@Slf4j
public class Account {

    private int id;
    private final String accountNumber;
    private final LocalDate dateCreated;
    private double balance;
    private final Lock balanceChangeLock;
    private final Condition sufficientFundsCondition;
    private String firstName;
    private String lastName;

    public Account(String accountNumber, LocalDate dateCreated, double balance, String firstName, String lastName) {
        this.accountNumber = accountNumber;
        this.dateCreated = dateCreated;
        this.balance = balance;
        this.balanceChangeLock = new ReentrantLock();
        sufficientFundsCondition = balanceChangeLock.newCondition();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void withdraw(double amount)
            throws InterruptedException {
        checkAmount(amount);

        balanceChangeLock.lock();
        try {
            while (balance < amount) {
                boolean timedOut = !sufficientFundsCondition.await(5L, TimeUnit.SECONDS);
                if (timedOut) {
                    throw new InsufficientBalanceException(String.format(DEFAULT_ISBE_MSG_FMT, this.accountNumber));
                }
            }
            log.info("Withdrawing {} from Account number {}", new Object[]{amount, this.accountNumber});
            balance -= amount;
            log.info(DEFAULT_NEW_BAL_MSG_FMT, new Object[]{this.balance, this.accountNumber});
        } finally {
            balanceChangeLock.unlock();
        }
    }

    private void checkAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidOperationException(DEFAULT_IOE_MSG_FMT);
        }
    }

    public void deposit(double amount) {
        checkAmount(amount);
        balanceChangeLock.lock();
        try {
            log.info("Depositing {} in Account {} ", new Object[]{amount, this.accountNumber});
            balance += amount;
            log.info(DEFAULT_NEW_BAL_MSG_FMT, new Object[]{balance, this.accountNumber});
            sufficientFundsCondition.signalAll();
        } finally {
            balanceChangeLock.unlock();
        }
    }
}
