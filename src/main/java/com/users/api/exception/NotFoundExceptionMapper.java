package com.users.api.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper extends BaseExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        if (exception.getCause() instanceof IllegalArgumentException cause) {
            boolean isInvalidUuid = cause.getMessage() != null
                    && cause.getMessage().startsWith("Invalid UUID string");
            String title = isInvalidUuid ? "Invalid UUID format" : "Invalid request parameter";
            return errorResponse(Response.Status.BAD_REQUEST, title);
        }
        return errorResponse(Response.Status.NOT_FOUND, "Resource not found");
    }
}
