package com.users.application.usecase;

import java.util.UUID;

import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetUserByIdUseCase {

    @Inject
    UserRepository userRepository;

    public User execute(UUID id) {
        Log.debugf("Looking up user with id: %s", id);
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        Log.debugf("User found with id: %s", id);
        return user;
    }
}
