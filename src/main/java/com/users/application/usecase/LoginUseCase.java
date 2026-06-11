package com.users.application.usecase;

import com.users.domain.exception.InvalidCredentialsException;
import com.users.domain.model.Credential;
import com.users.domain.model.User;
import com.users.domain.port.persistence.CredentialRepository;
import com.users.domain.port.persistence.UserRepository;
import com.users.domain.port.security.PasswordHasher;
import com.users.domain.port.security.TokenIssuer;
import com.users.domain.port.security.TokenIssuer.IssuedToken;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoginUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    CredentialRepository credentialRepository;

    @Inject
    PasswordHasher passwordHasher;

    @Inject
    TokenIssuer tokenIssuer;

    public IssuedToken execute(String email, String rawPassword) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
        Credential credential = credentialRepository.findByUserId(user.getId())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(rawPassword, credential.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        Log.infof("User logged in: %s", user.getId());
        return tokenIssuer.issue(user.getId(), user.getEmail(), credential.getRole());
    }
}
