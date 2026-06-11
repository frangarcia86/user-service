package com.users.application.usecase;

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
import com.users.domain.port.persistence.UserRepository;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    DeleteUserUseCase deleteUserUseCase;

    @Test
    void deletesExistingUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Anton", "antonio@mail.com");

        when(userRepository.findUserById(id)).thenReturn(Optional.of(user));

        deleteUserUseCase.execute(id);

        verify(userRepository).findUserById(id);
        verify(userRepository).removeById(id);
    }

    @Test
    void throwsWhenUserNotFound() {
        UUID id = UUID.randomUUID();

        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteUserUseCase.execute(id))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findUserById(id);
    }
}
