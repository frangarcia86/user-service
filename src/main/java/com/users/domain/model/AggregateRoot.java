package com.users.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class AggregateRoot {

    private final UUID id;
    private final Instant createdAt;

    protected AggregateRoot(UUID id, Instant createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
