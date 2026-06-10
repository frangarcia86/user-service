package com.users.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.users.domain.model.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findUserById(UUID id);
    boolean existsByEmail(String email);
    User update(User user);
    User replace(User user);
    void removeById(UUID id);
}
