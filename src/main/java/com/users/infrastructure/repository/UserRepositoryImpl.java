package com.users.infrastructure.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;
import com.users.infrastructure.mapper.UserMapper;
import com.users.infrastructure.persistence.UserEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserRepositoryImpl
        implements UserRepository, PanacheRepositoryBase<UserEntity, UUID> {

    @Inject
    UserMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        entity.setCreatedAt(Instant.now());
        persist(entity);
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
        UserEntity entity = findById(user.getId());
        entity.setName(user.getName());
        entity.setBirthDate(user.getBirthDate());
        entity.setPhone(user.getPhone());
        entity.setAddress(user.getAddress());
        entity.setPostalCode(user.getPostalCode());
        return mapper.toDomain(entity);
    }

    @Override
    public void removeById(UUID id) {
        delete("id = ?1", id);
    }
}
