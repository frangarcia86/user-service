package com.users.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.users.domain.exception.EmailAlreadyExistsException;

import jakarta.ws.rs.core.Response;

class EmailAlreadyExistsExceptionMapperTest {

    private final EmailAlreadyExistsExceptionMapper mapper = new EmailAlreadyExistsExceptionMapper();

    @Test
    void toResponse_returns409Status() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("antonio@mail.com");

        Response response = mapper.toResponse(exception);

        assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void toResponse_returnsErrorResponseWithCorrectFields() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("antonio@mail.com");

        Response response = mapper.toResponse(exception);

        assertThat(response.getEntity()).isInstanceOf(ErrorResponse.class);
        ErrorResponse body = (ErrorResponse) response.getEntity();
        assertThat(body.title()).isEqualTo("Email Already Exists");
        assertThat(body.status()).isEqualTo(409);
    }
}
