package com.users.application.usecase;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.port.NotificationPort;
import com.users.domain.repository.UserRepository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetUserByIdUseCase {

    @ConfigProperty(name = "user.access-alert.threshold-seconds", defaultValue = "60")
    long accessAlertThresholdSeconds;

    @Inject
    UserRepository userRepository;

    @Inject
    NotificationPort notificationPort;

    public User execute(UUID id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        Log.debugf("User found with id: %s", id);

        triggerAccessAlertIfNeeded(user);
        
        return user;
    }

    private void triggerAccessAlertIfNeeded(User user) {
        if (user.getCreatedAt() == null) {
            return;
        }

        long secondsSinceCreation = Duration.between(user.getCreatedAt(), Instant.now()).getSeconds();
        if (secondsSinceCreation > accessAlertThresholdSeconds) {
            Log.infof("Access alert triggered for user %s (%ds old)", user.getId(), secondsSinceCreation);
            notificationPort.notifyAccessAlert(user);
        }
    }
}
