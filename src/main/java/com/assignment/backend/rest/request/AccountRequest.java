package com.assignment.backend.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    @Size(min = 3, message = "Account Number is mandatory with minimum 3 numbers")
    private String accountNumber;

    @Min(value = 1, message = "Minimum Amount to operation is 1")
    private double amount;

}
