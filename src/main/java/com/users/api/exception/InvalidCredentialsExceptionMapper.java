package com.users.api.exception;

import com.users.domain.exception.InvalidCredentialsException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidCredentialsExceptionMapper extends BaseExceptionMapper<InvalidCredentialsException> {

    @Override
    public Response toResponse(InvalidCredentialsException exception) {
        return errorResponse(Response.Status.UNAUTHORIZED, "Invalid Credentials");
    }
}
