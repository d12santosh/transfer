package com.assignment.backend.rest;

import com.assignment.backend.domain.repo.AccountRepository;
import com.assignment.backend.domain.service.AccountService;
import com.assignment.backend.rest.request.AccountRequest;
import com.assignment.backend.rest.request.CreateAccountRequest;
import com.assignment.backend.rest.request.TransferRequest;
import com.assignment.backend.rest.response.AccountInfoResponse;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("account")
@Slf4j
@Singleton
public class Transfer {


    private AccountService accountService;

    @Inject
    public Transfer( AccountService accountService) {
        this.accountService = accountService;
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
    public Response createAccount(@Valid CreateAccountRequest request) {
        log.info("Received request to create account with details {}", request);
        return Response.ok(accountService.createAccount(request), MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response withdrawAmount(@Valid AccountRequest request) {
        log.info("Received request to withdraw amount for account with details {}", request);
        return Response.ok(accountService.withdrawAmountFromAccount(request), MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response depositAmount(@Valid AccountRequest request) {
        log.info("Received request to deposit amount for account with details {}", request);
        return Response.ok(accountService.depositAmountToAccount(request), MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response depositAmount() {
        log.info("Received request for list of account");
        GenericEntity entity = new GenericEntity<List<AccountInfoResponse>>(accountService.getAllAccounts()) {
        };
        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }
}
