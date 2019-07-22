package com.assignment.backend.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class Account {

    private final String accountNumber;
    private final LocalDate dateCreated;
    private double balance;
    private Lock balanceChangeLock;
    private Condition sufficientFundsCondition;



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
            while (balance < amount)
                sufficientFundsCondition.await();
            System.out.print("Withdrawing " + amount);
            double newBalance = balance - amount;
            System.out.println(", new balance is " + newBalance);
            balance = newBalance;
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
            System.out.print("Depositing " + amount);
            double newBalance = balance + amount;
            System.out.println(", new balance is " + newBalance);
            balance = newBalance;
            sufficientFundsCondition.signalAll();
        }
        finally
        {
            balanceChangeLock.unlock();
        }
    }
}
