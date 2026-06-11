package com.users.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.port.notification.NotificationPort;
import com.users.domain.port.persistence.UserRepository;

@ExtendWith(MockitoExtension.class)
class GetUserByIdUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    NotificationPort notificationPort;

    @InjectMocks
    GetUserByIdUseCase getUserByIdUseCase;

    @BeforeEach
    void setUp() {
        getUserByIdUseCase.accessAlertThresholdSeconds = 60L;
    }

    @Test
    void execute_returnsUser_whenFound() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Isabel Fernandez", "isabel.fernandez@mail.com");

        when(userRepository.findUserById(id)).thenReturn(Optional.of(user));

        User result = getUserByIdUseCase.execute(id);

        assertThat(result).isEqualTo(user);
        verify(userRepository).findUserById(id);
    }

    @Test
    void execute_throwsUserNotFoundException_whenNotFound() {
        UUID id = UUID.randomUUID();

        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserByIdUseCase.execute(id))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findUserById(id);
    }

    @Test
    void execute_triggersAccessAlert_whenUserCreatedBeforeThreshold() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Isabel Fernandez", "isabel.fernandez@mail.com");
        user.setCreatedAt(Instant.now().minusSeconds(120));

        when(userRepository.findUserById(id)).thenReturn(Optional.of(user));

        getUserByIdUseCase.execute(id);

        verify(notificationPort).notifyAccessAlert(user);
    }

    @Test
    void execute_doesNotTriggerAccessAlert_whenUserCreatedWithinThreshold() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Isabel Fernandez", "isabel.fernandez@mail.com");
        user.setCreatedAt(Instant.now().minusSeconds(30));

        when(userRepository.findUserById(id)).thenReturn(Optional.of(user));

        getUserByIdUseCase.execute(id);

        verify(notificationPort, never()).notifyAccessAlert(any());
    }

    @Test
    void execute_doesNotTriggerAccessAlert_whenCreatedAtIsNull() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Isabel Fernandez", "isabel.fernandez@mail.com");

        when(userRepository.findUserById(id)).thenReturn(Optional.of(user));

        getUserByIdUseCase.execute(id);

        verify(notificationPort, never()).notifyAccessAlert(any());
    }
}
