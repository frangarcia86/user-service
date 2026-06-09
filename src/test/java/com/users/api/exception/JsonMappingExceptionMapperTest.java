package com.users.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.ws.rs.core.Response;

class JsonMappingExceptionMapperTest {

    private final JsonMappingExceptionMapper mapper = new JsonMappingExceptionMapper();

    @Test
    void toResponse_returns400Status() {
        InvalidFormatException exception = new InvalidFormatException(null, "Invalid value", "abc", Integer.class);

        Response response = mapper.toResponse(exception);

        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    void toResponse_returnsErrorResponseWithFieldName() {
        InvalidFormatException exception = new InvalidFormatException(null, "Invalid value", "abc", Integer.class);
        exception.prependPath(new JsonMappingException.Reference(null, "postalCode"));

        Response response = mapper.toResponse(exception);

        assertThat(response.getEntity()).isInstanceOf(ErrorResponse.class);
        ErrorResponse body = (ErrorResponse) response.getEntity();
        assertThat(body.title()).contains("postalCode");
        assertThat(body.status()).isEqualTo(400);
    }

    @Test
    void toResponse_returnsUnknownField_whenPathIsEmpty() {
        InvalidFormatException exception = new InvalidFormatException(null, "Invalid value", "abc", Integer.class);

        Response response = mapper.toResponse(exception);

        ErrorResponse body = (ErrorResponse) response.getEntity();
        assertThat(body.title()).contains("unknown");
    }
}
