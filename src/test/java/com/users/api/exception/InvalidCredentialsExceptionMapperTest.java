package com.users.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.users.domain.exception.InvalidCredentialsException;

import jakarta.ws.rs.core.Response;

class InvalidCredentialsExceptionMapperTest {

    private final InvalidCredentialsExceptionMapper mapper = new InvalidCredentialsExceptionMapper();

    @Test
    void toResponse_returns401Status() {
        Response response = mapper.toResponse(new InvalidCredentialsException());

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    void toResponse_returnsErrorResponseWithCorrectFields() {
        Response response = mapper.toResponse(new InvalidCredentialsException());

        var body = (BaseExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(body.title()).isEqualTo("Invalid Credentials");
        assertThat(body.status()).isEqualTo(401);
    }
}
