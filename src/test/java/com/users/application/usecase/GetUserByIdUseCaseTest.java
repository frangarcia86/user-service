package com.users.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class GetUserByIdUseCaseTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    GetUserByIdUseCase getUserByIdUseCase;

    @Test
    void execute_returnsUserWhenRepositoryFindsIt() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Anton", "antonio@mail.com", Instant.now());

        when(userRepository.findUserById(id)).thenReturn(Optional.of(user));

        Optional<User> result = getUserByIdUseCase.execute(id);

        assertThat(result).contains(user);
        verify(userRepository).findUserById(id);
    }

    @Test
    void execute_returnsEmptyWhenRepositoryDoesNotFindIt() {
        UUID id = UUID.randomUUID();

        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        Optional<User> result = getUserByIdUseCase.execute(id);

        assertThat(result).isEmpty();
        verify(userRepository).findUserById(id);
    }
}
