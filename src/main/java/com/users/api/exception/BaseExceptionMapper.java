package com.users.api.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public abstract class BaseExceptionMapper<E extends Exception> implements ExceptionMapper<E> {

    public record ErrorResponse(String title, int status) {}

    protected Response errorResponse(Response.Status status, String title) {
        return Response.status(status)
                .entity(new ErrorResponse(title, status.getStatusCode()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
