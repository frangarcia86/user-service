package com.users.application.usecase;

import com.users.domain.exception.EmailAlreadyExistsException;
import com.users.domain.model.User;
import com.users.domain.port.AddressVerificationPort;
import com.users.domain.port.AddressVerificationResult;
import com.users.domain.repository.UserRepository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    AddressVerificationPort addressVerificationPort;

    @Transactional
    public User execute(User user) {
        Log.debugf("Checking email uniqueness for: %s", user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            Log.warnf("Email already exists: %s", user.getEmail());
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        enrichAddressData(user);

        User saved = userRepository.save(user);
        Log.infof("User saved with id: %s", saved.getId());
        return saved;
    }

    private void enrichAddressData(User user) {
        if (user.getAddress() == null) {
            return;
        }
        AddressVerificationResult result = addressVerificationPort.verify(user);
        user.setAddress(result.address());
        user.setPostalCode(result.postalCode());
        Log.debugf("Address enriched for user '%s': address='%s', postalCode=%d",
                user.getName(), result.address(), result.postalCode());
    }
}

