package com.users.application.usecase;

import java.util.UUID;

import com.users.domain.exception.UserNotFoundException;
import com.users.domain.repository.UserRepository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeleteUserUseCase {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void execute(UUID id) {
        if (userRepository.findUserById(id).isEmpty()) {
            Log.warnf("User not found for deletion with id: %s", id);
            throw new UserNotFoundException(id);
        }

        userRepository.removeById(id);
        Log.infof("User deleted: %s", id);
    }
}
