package com.users.api.exception;

import com.users.domain.exception.EmailAlreadyExistsException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EmailAlreadyExistsExceptionMapper extends BaseExceptionMapper<EmailAlreadyExistsException> {

    @Override
    public Response toResponse(EmailAlreadyExistsException exception) {
        return errorResponse(Response.Status.CONFLICT, "Email Already Exists");
    }
}
