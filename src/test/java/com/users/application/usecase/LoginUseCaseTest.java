package com.users.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.exception.InvalidCredentialsException;
import com.users.domain.model.Credential;
import com.users.domain.model.Role;
import com.users.domain.model.User;
import com.users.domain.port.persistence.CredentialRepository;
import com.users.domain.port.persistence.UserRepository;
import com.users.domain.port.security.PasswordHasher;
import com.users.domain.port.security.TokenIssuer;
import com.users.domain.port.security.TokenIssuer.IssuedToken;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CredentialRepository credentialRepository;

    @Mock
    PasswordHasher passwordHasher;

    @Mock
    TokenIssuer tokenIssuer;

    @InjectMocks
    LoginUseCase loginUseCase;

    @Test
    void execute_returnsToken_whenCredentialsAreValid() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Ana", "ana@mail.com");
        Credential credential = new Credential(userId, "hashed", Role.USER);
        IssuedToken expected = new IssuedToken("jwt.token.value", 3600L);

        when(userRepository.findUserByEmail("ana@mail.com")).thenReturn(Optional.of(user));
        when(credentialRepository.findByUserId(userId)).thenReturn(Optional.of(credential));
        when(passwordHasher.matches("raw", "hashed")).thenReturn(true);
        when(tokenIssuer.issue(userId, "ana@mail.com", Role.USER)).thenReturn(expected);

        IssuedToken result = loginUseCase.execute("ana@mail.com", "raw");

        assertThat(result).isEqualTo(expected);
        verify(tokenIssuer).issue(userId, "ana@mail.com", Role.USER);
    }

    @Test
    void execute_throwsInvalidCredentials_whenUserNotFound() {
        when(userRepository.findUserByEmail("missing@mail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUseCase.execute("missing@mail.com", "raw"))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(tokenIssuer, never()).issue(any(), any(), any());
    }

    @Test
    void execute_throwsInvalidCredentials_whenCredentialMissing() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Ana", "ana@mail.com");

        when(userRepository.findUserByEmail("ana@mail.com")).thenReturn(Optional.of(user));
        when(credentialRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUseCase.execute("ana@mail.com", "raw"))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(tokenIssuer, never()).issue(any(), any(), any());
    }

    @Test
    void execute_throwsInvalidCredentials_whenPasswordDoesNotMatch() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Ana", "ana@mail.com");
        Credential credential = new Credential(userId, "hashed", Role.USER);

        when(userRepository.findUserByEmail("ana@mail.com")).thenReturn(Optional.of(user));
        when(credentialRepository.findByUserId(userId)).thenReturn(Optional.of(credential));
        when(passwordHasher.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> loginUseCase.execute("ana@mail.com", "wrong"))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(tokenIssuer, never()).issue(any(), any(), any());
    }
}
