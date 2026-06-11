package com.users.domain.model;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "userId")
public class Credential {

    private final UUID userId;
    private final String passwordHash;
    private final Role role;

    public Credential(UUID userId, String passwordHash, Role role) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
