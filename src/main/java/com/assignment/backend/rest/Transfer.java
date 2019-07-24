package com.assignment.backend.rest;

import com.assignment.backend.dao.AccountDAO;
import com.assignment.backend.dao.impl.SavingsAccountDAOImpl;
import com.assignment.backend.service.AccountService;
import com.assignment.backend.service.impl.SavingsAccountServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("account")
@Slf4j
@Singleton
public class Transfer {

    public static final String CLICHED_MESSAGE = "Hello World!";

    private AccountDAO accountDAO;
    private AccountService accountService;

    @Inject
    public Transfer() {
        log.info("Constructor is called");
        accountDAO = new SavingsAccountDAOImpl();
        accountService = new SavingsAccountServiceImpl(accountDAO);
    }

    @POST
    @Path("transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response transfer(@Valid TransferRequest request) {
        log.info("Received request to transfer amount with details {}", request);
        return Response.ok(accountService.transfer(request), MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createAccount(@Valid AccountRequest request) {
        log.info("Received request to create account with details {}", request);
        return  Response.ok(accountService.createAccount(request), MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response withdrawAmount(@Valid AccountRequest request) {
        log.info("Received request to withdraw amount for account with details {}", request);
        return  Response.ok(accountService.withdrawAmountFromAccount(request), MediaType.TEXT_PLAIN).build();
    }
}
