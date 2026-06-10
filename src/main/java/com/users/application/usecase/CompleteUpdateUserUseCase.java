package com.users.application.usecase;

import java.util.UUID;

import com.users.application.dto.UserUpdateData;
import com.users.application.mapper.UserUpdateMapper;
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

    @Inject
    UserUpdateMapper userUpdateMapper;

    @Transactional
    public User execute(UUID id, UserUpdateData data) {
        User existing = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userUpdateMapper.applyUpdate(data, existing);

        return userRepository.update(existing);
    }
}
