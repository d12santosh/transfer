package com.assignment.backend.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoResponse {


    private String accountNumber;

    private String firstName;

    private String lastName;

    private double balance;

}
