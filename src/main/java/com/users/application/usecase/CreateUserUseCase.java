package com.users.application.usecase;

import com.users.domain.exception.EmailAlreadyExistsException;
import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User execute(User user) {
        Log.debugf("Checking email uniqueness for: %s", user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            Log.warnf("Email already exists: %s", user.getEmail());
            throw new EmailAlreadyExistsException(user.getEmail());
        }
        User saved = userRepository.save(user);
        Log.infof("User saved with id: %s", saved.getId());
        return saved;
    }
}
