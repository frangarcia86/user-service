package com.users.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.exception.EmailAlreadyExistsException;
import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CreateUserUseCase createUserUseCase;

    @Test
    void execute_savesUserAndReturnsSavedUser() {
        UUID id = UUID.randomUUID();

        User input = new User(id, "Anton", "antonio@mail.com");
        User saved = new User(id, "Anton", "antonio@mail.com");

        when(userRepository.existsByEmail("antonio@mail.com")).thenReturn(false);
        when(userRepository.save(input)).thenReturn(saved);

        User result = createUserUseCase.execute(input);

        assertThat(result).isEqualTo(saved);
        verify(userRepository).save(input);
    }

    @Test
    void execute_throwsEmailAlreadyExistsException_whenEmailIsDuplicated() {
        UUID id = UUID.randomUUID();
        User input = new User(id, "Anton", "antonio@mail.com");

        when(userRepository.existsByEmail("antonio@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> createUserUseCase.execute(input))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }
}
