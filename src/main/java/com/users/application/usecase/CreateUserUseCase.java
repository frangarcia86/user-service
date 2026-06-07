package com.users.application.usecase;

import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User execute(User user) {
        return userRepository.save(user);
    }
}
