package com.assignment.backend.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AccountRequest {

    @Size(min = 3, message = "Account Number is mandatory with minimum 3 numbers")
    private String accountNumber;

    @Min(value = 1, message = "Minimum Amount to operation is 1")
    private double amount;

}
