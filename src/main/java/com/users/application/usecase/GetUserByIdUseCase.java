package com.users.application.usecase;

import java.util.Optional;
import java.util.UUID;

import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetUserByIdUseCase {

    @Inject
    UserRepository userRepository;

    public Optional<User> execute(UUID id) {
        return userRepository.findUserById(id);
    }
}
