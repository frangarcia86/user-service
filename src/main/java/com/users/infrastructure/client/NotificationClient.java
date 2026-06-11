package com.users.infrastructure.client;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.users.domain.model.User;
import com.users.domain.port.NotificationPort;
import com.users.infrastructure.client.notification.dto.AccessAlertRequest;
import com.users.infrastructure.client.notification.rest.NotificationRestClient;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class NotificationClient implements NotificationPort {

    @Inject
    @RestClient
    NotificationRestClient notificationRestClient;

    @Override
    public void notifyAccessAlert(User user) {
        Log.infof("Sending access alert notification for user '%s' <%s> (id: %s)",
                user.getName(), user.getEmail(), user.getId());

        AccessAlertRequest request = AccessAlertRequest.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();

        // Notification Service is simulated — no real endpoint exists for this demo.
        try (Response response = notificationRestClient.sendAccessAlert(request)) {
            Log.debugf("Access alert response status: %d for user id: %s", response.getStatus(), user.getId());
        } catch (Exception e) {
            Log.warnf("Failed to send access alert for user id %s: %s", user.getId(), e.getMessage());
        }
    }
}

