package com.assignment.backend.rest.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class TransferRequest {

    @Size(min = 3, message = "Account Number is mandatory with minimum 3 numbers")
    private String fromAccount;
    @Size(min = 3, message = "Account Number is mandatory with minimum 3 numbers")
    private String toAccount;
    @Min(value = 1, message = "Minimum Amount to transfer is 1")
    private double amount;

    public TransferRequest(String fromAccount, String toAccount, double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }
}
