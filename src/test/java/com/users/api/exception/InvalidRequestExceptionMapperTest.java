package com.users.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

class InvalidRequestExceptionMapperTest {

    private final InvalidRequestExceptionMapper mapper = new InvalidRequestExceptionMapper();

    @Test
    void toResponse_returns400_whenCausedByInvalidUuid() {
        NotFoundException exception = new NotFoundException(new IllegalArgumentException("Invalid UUID string: abc"));

        Response response = mapper.toResponse(exception);

        assertThat(response.getStatus()).isEqualTo(400);
        var body = (BaseExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(body.title()).isEqualTo("Invalid UUID format");
        assertThat(body.status()).isEqualTo(400);
    }

    @Test
    void toResponse_returns400_whenCausedByOtherIllegalArgument() {
        NotFoundException exception = new NotFoundException(new IllegalArgumentException("Some other bad argument"));

        Response response = mapper.toResponse(exception);

        assertThat(response.getStatus()).isEqualTo(400);
        var body = (BaseExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(body.title()).isEqualTo("Invalid request parameter");
        assertThat(body.status()).isEqualTo(400);
    }

    @Test
    void toResponse_returns404_whenNoCause() {
        NotFoundException exception = new NotFoundException();

        Response response = mapper.toResponse(exception);

        assertThat(response.getStatus()).isEqualTo(404);
        var body = (BaseExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(body.title()).isEqualTo("Resource not found");
        assertThat(body.status()).isEqualTo(404);
    }
}
