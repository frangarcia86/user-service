package com.users.domain.port.security;

import java.util.UUID;

import com.users.domain.model.Role;

public interface TokenIssuer {

    IssuedToken issue(UUID userId, String email, Role role);

    record IssuedToken(String token, long expiresInSeconds) {}
}
