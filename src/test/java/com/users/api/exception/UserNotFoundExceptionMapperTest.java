package com.users.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.users.domain.exception.UserNotFoundException;

import jakarta.ws.rs.core.Response;

class UserNotFoundExceptionMapperTest {

    private final UserNotFoundExceptionMapper mapper = new UserNotFoundExceptionMapper();

    @Test
    void toResponse_returns404Status() {
        UserNotFoundException exception = new UserNotFoundException(UUID.randomUUID());

        Response response = mapper.toResponse(exception);

        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void toResponse_returnsErrorResponseWithCorrectFields() {
        UserNotFoundException exception = new UserNotFoundException(UUID.randomUUID());

        Response response = mapper.toResponse(exception);

        var body = (BaseExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(body.title()).isEqualTo("User Not Found");
        assertThat(body.status()).isEqualTo(404);
    }
}
