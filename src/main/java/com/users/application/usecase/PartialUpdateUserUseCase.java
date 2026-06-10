package com.users.application.usecase;

import java.util.UUID;

import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PartialUpdateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Transactional
    public User execute(UUID id, UserUpdateData data) {
        User existing = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (data.name() != null) existing.setName(data.name());
        if (data.birthDate() != null) existing.setBirthDate(data.birthDate());
        if (data.phone() != null) existing.setPhone(data.phone());
        if (data.address() != null) existing.setAddress(data.address());
        if (data.postalCode() != null) existing.setPostalCode(data.postalCode());

        return userRepository.update(existing);
    }
}
