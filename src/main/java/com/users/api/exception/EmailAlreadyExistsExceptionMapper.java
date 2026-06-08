package com.users.api.exception;

import com.users.domain.exception.EmailAlreadyExistsException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EmailAlreadyExistsExceptionMapper implements ExceptionMapper<EmailAlreadyExistsException> {

    @Override
    public Response toResponse(EmailAlreadyExistsException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse("Email Already Exists", 409))
                .build();
    }
}
