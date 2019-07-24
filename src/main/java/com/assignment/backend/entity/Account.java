package com.assignment.backend.entity;

import com.assignment.backend.exceptions.InsufficientBalanceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@Slf4j
public class Account {

    private final String accountNumber;
    private final LocalDate dateCreated;
    private double balance;
    private final Lock balanceChangeLock;
    private final Condition sufficientFundsCondition;



    public Account(String accountNumber, LocalDate dateCreated, double balance) {
        this.accountNumber = accountNumber;
        this.dateCreated = dateCreated;
        this.balance = balance;
        this.balanceChangeLock = new ReentrantLock();
        sufficientFundsCondition = balanceChangeLock.newCondition();
    }

    public void withdraw(double amount)
            throws InterruptedException
    {
        balanceChangeLock.lock();
        try
        {
            while (balance < amount) {
                boolean timedOut = !sufficientFundsCondition.await(5L, TimeUnit.SECONDS);
                if(timedOut){
                    throw new InsufficientBalanceException(String.format("Insufficient Balance in Account with number %s to perform withdraw  Operation", this.accountNumber));
                }
            }
            log.info("Withdrawing {} from Account number {}", new Object[]{amount, this.accountNumber});
            balance -= amount;
            log.info("New balance after Withdrawing {} in Account {}", new Object[]{this.balance, this.accountNumber});
        }
        finally
        {
            balanceChangeLock.unlock();
        }
    }

    public void deposit(double amount)
    {
        balanceChangeLock.lock();
        try
        {
            log.info("Depositing {} in Account {} ", new Object[]{amount, this.accountNumber});
            balance += amount;
            log.info("New balance after depositing {} in Account {} ", new Object[]{balance, this.accountNumber});
            sufficientFundsCondition.signalAll();
        }
        finally
        {
            balanceChangeLock.unlock();
        }
    }
}
