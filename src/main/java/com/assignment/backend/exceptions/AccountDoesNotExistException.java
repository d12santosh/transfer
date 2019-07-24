package com.assignment.backend.exceptions;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class AccountDoesNotExistException extends RuntimeException implements ExceptionMapper<AccountDoesNotExistException> {

    public AccountDoesNotExistException(String message) {
        super(message);
    }

    public AccountDoesNotExistException() {
        super();
    }

    @Override
    public Response toResponse(AccountDoesNotExistException exception) {
        log.warn("AccountDoesNotExistException thrown with following reason-->", exception);
        return Response.status(400).entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN).build();
    }
}
