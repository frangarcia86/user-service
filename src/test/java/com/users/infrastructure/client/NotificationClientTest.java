package com.users.infrastructure.client;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.model.User;
import com.users.infrastructure.client.notification.rest.NotificationRestClient;

import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class NotificationClientTest {

    @Mock
    NotificationRestClient notificationRestClient;

    @InjectMocks
    NotificationClient notificationClient;

    @Test
    void notifyAccessAlert_sendsRequestWithCorrectFields() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Isabel Fernandez", "isabel.fernandez@mail.com");
        user.setCreatedAt(Instant.now());

        Response mockResponse = Response.accepted().build();
        when(notificationRestClient.sendAccessAlert(argThat(request ->
                request.getUserId().equals(id) &&
                request.getEmail().equals("isabel.fernandez@mail.com") &&
                request.getName().equals("Isabel Fernandez")
        ))).thenReturn(mockResponse);

        notificationClient.notifyAccessAlert(user);

        verify(notificationRestClient).sendAccessAlert(argThat(request ->
                request.getUserId().equals(id) &&
                request.getEmail().equals("isabel.fernandez@mail.com") &&
                request.getName().equals("Isabel Fernandez")
        ));
    }

    @Test
    void notifyAccessAlert_doesNotPropagateException_whenRestClientFails() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Isabel Fernandez", "isabel.fernandez@mail.com");

        when(notificationRestClient.sendAccessAlert(argThat(r -> true)))
                .thenThrow(new RuntimeException("Connection refused"));

        assertThatCode(() -> notificationClient.notifyAccessAlert(user))
                .doesNotThrowAnyException();
    }
}
