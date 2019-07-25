package com.assignment.backend.application.exceptions;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class InsufficientBalanceException extends RuntimeException implements ExceptionMapper<InsufficientBalanceException> {
    public InsufficientBalanceException(String msg) {
        super(msg);
    }

    public InsufficientBalanceException() {
        super();
    }

    @Override
    public Response toResponse(InsufficientBalanceException exception) {
        log.warn("InsufficientBalanceException thrown with following stacktrace-->", exception);
        return Response.status(400).entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN).build();
    }
}
