package com.users.application.usecase;

import java.util.UUID;

import com.users.application.dto.UserUpdateData;
import com.users.application.mapper.UserUpdateMapper;
import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PartialUpdateUserUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    UserUpdateMapper userUpdateMapper;

    @Transactional
    public User execute(UUID id, UserUpdateData data) {
        Log.debugf("Looking up user for partial update with id: %s", id);
        User existing = userRepository.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userUpdateMapper.applyPartialUpdate(data, existing);
        User updated = userRepository.update(existing);
        
        Log.infof("User partially updated with id: %s", id);
        return updated;
    }
}
