package com.users.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonMappingExceptionMapper extends BaseExceptionMapper<InvalidFormatException> {

    @Override
    public Response toResponse(InvalidFormatException exception) {
        String field = exception.getPath().isEmpty() ? "unknown" : exception.getPath().get(0).getFieldName();
        String message = "Invalid value for field '" + field + "': " + exception.getValue();

        return errorResponse(Response.Status.BAD_REQUEST, message);
    }
}
