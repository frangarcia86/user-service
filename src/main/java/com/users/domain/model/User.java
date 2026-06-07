package com.users.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class User extends AggregateRoot {

    private final String name;
    private final String email;

    public User(UUID id, String name, String email, Instant createdAt) {
        super(id, createdAt);
        this.name = name;
        this.email = email;
    }

    public static User create(String name, String email) {
        return new User(UUID.randomUUID(), name, email, Instant.now());
    }
}
