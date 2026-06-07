package com.users.domain.model;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class AggregateRoot {

    private final UUID id;
    private final Instant createdAt;

    protected AggregateRoot(UUID id, Instant createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
