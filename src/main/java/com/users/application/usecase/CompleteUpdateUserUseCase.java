package com.users.application.usecase;

import java.util.UUID;

import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CompleteUpdateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User execute(UUID id, UserUpdateData data) {
        User existing = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        existing.setName(data.name());
        existing.setBirthDate(data.birthDate());
        existing.setPhone(data.phone());
        existing.setAddress(data.address());
        existing.setPostalCode(data.postalCode());

        return userRepository.update(existing);
    }
}
