package com.users.infrastructure.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;
import com.users.infrastructure.mapper.UserEntityMapper;
import com.users.infrastructure.persistence.UserEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserRepositoryImpl
        implements UserRepository, PanacheRepositoryBase<UserEntity, UUID> {

    @Inject
    UserEntityMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        entity.setCreatedAt(Instant.now());
        persist(entity);
        
        Log.debugf("User persisted with id: %s", entity.getId());
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return findByIdOptional(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }

    @Override
    public User update(User user) {
        Log.debugf("Updating entity for user id: %s", user.getId());
        UserEntity entity = findById(user.getId());
        mapper.updateEntity(user, entity);
        return mapper.toDomain(entity);
    }

    @Override
    public User replace(User user) {
        UserEntity entity = findById(user.getId());
        mapper.replaceEntity(user, entity);
        return mapper.toDomain(entity);
    }

    @Override
    public void removeById(UUID id) {
        deleteById(id);
    }
}
