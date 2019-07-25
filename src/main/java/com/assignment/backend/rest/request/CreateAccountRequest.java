package com.assignment.backend.rest.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateAccountRequest {

    @Size(min = 3, message = "Account Number is mandatory with minimum 3 numbers")
    private String accountNumber;

    @Min(value = 1, message = "Minimum Amount to operation is 1")
    private double amount;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    public CreateAccountRequest(@Size(min = 3, message = "Account Number is mandatory with minimum 3 numbers") String accountNumber,
                                @Min(value = 1, message = "Minimum Amount to operation is 1") double amount,
                                @NotBlank(message = "First name is mandatory") String firstName,
                                @NotBlank(message = "Last name is mandatory") String lastName) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
