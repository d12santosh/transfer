package com.assignment.backend.application.exceptions;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Slf4j
public class InvalidOperationException extends RuntimeException implements ExceptionMapper<InvalidOperationException> {
    public InvalidOperationException() {
        super();
    }

    public InvalidOperationException(String message) {
        super(message);
    }

    @Override
    public Response toResponse(InvalidOperationException exception) {
        log.warn("InvalidOperationException thrown with following stacktrace-->", exception);
        return Response.status(400).entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN).build();
    }
}
