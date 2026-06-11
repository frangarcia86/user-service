package com.users.infrastructure.client.notification;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.users.domain.model.User;
import com.users.domain.port.notification.NotificationPort;
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
        AccessAlertRequest request = AccessAlertRequest.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();

        // Notification Service is simulated (no real endpoint exists)
        try (Response response = notificationRestClient.sendAccessAlert(request)) {
            Log.infof("Access alert sent for user %s (status=%d)", user.getId(), response.getStatus());
        } catch (Exception e) {
            Log.warnf("Failed to send access alert for user %s: %s", user.getId(), e.getMessage());
        }
    }
}

