package com.users.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<InvalidFormatException> {

    @Override
    public Response toResponse(InvalidFormatException exception) {
        String field = exception.getPath().isEmpty() ? "unknown" : exception.getPath().get(0).getFieldName();
        String message = "Invalid value for field '" + field + "': " + exception.getValue();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(message, 400))
                .build();
    }
}
