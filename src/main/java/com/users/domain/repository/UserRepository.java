package com.users.domain.repository;

import com.users.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findUserById(UUID id);
    boolean existsByEmail(String email);
}
