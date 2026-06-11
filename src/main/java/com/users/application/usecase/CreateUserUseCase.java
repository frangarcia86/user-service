package com.users.application.usecase;

import com.users.domain.exception.EmailAlreadyExistsException;
import com.users.domain.model.Credential;
import com.users.domain.model.Role;
import com.users.domain.model.User;
import com.users.domain.port.address.AddressVerificationPort;
import com.users.domain.port.address.AddressVerificationResult;
import com.users.domain.port.persistence.CredentialRepository;
import com.users.domain.port.persistence.UserRepository;
import com.users.domain.port.security.PasswordHasher;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    CredentialRepository credentialRepository;

    @Inject
    PasswordHasher passwordHasher;

    @Inject
    AddressVerificationPort addressVerificationPort;

    @Transactional
    public User execute(User user, String rawPassword) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        enrichAddressData(user);
        User saved = userRepository.save(user);

        Credential credential = new Credential(saved.getId(), passwordHasher.hash(rawPassword), Role.USER);
        credentialRepository.save(credential);

        Log.infof("User saved: %s", saved.getId());
        return saved;
    }

    private void enrichAddressData(User user) {
        if (user.getAddress() == null) {
            return;
        }
        
        AddressVerificationResult result = addressVerificationPort.verify(user);
        user.setAddress(result.address());
        user.setPostalCode(result.postalCode());
    }
}

