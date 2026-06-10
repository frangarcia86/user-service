package com.users.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class GetUserByIdUseCaseTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    GetUserByIdUseCase getUserByIdUseCase;

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
}
