package com.users.api.exception;

import com.users.domain.exception.UserNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserNotFoundExceptionMapper extends BaseExceptionMapper<UserNotFoundException> {

    @Override
    public Response toResponse(UserNotFoundException exception) {
        return errorResponse(Response.Status.NOT_FOUND, "User Not Found");
    }
}
