package com.users.domain.port.persistence;

import java.util.Optional;
import java.util.UUID;

import com.users.domain.model.Credential;

public interface CredentialRepository {
    Credential save(Credential credential);
    Optional<Credential> findByUserId(UUID userId);
}
