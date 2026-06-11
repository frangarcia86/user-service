package com.users.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import com.users.domain.model.Credential;
import com.users.domain.port.persistence.CredentialRepository;
import com.users.infrastructure.persistence.entity.CredentialEntity;
import com.users.infrastructure.persistence.mapper.CredentialEntityMapper;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CredentialRepositoryImpl
        implements CredentialRepository, PanacheRepositoryBase<CredentialEntity, UUID> {

    @Inject
    CredentialEntityMapper mapper;

    @Override
    public Credential save(Credential credential) {
        CredentialEntity entity = mapper.toEntity(credential);
        persist(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Credential> findByUserId(UUID userId) {
        return findByIdOptional(userId).map(mapper::toDomain);
    }
}
