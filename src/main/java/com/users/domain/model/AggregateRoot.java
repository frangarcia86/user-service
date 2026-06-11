package com.users.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "id")
public abstract class AggregateRoot {

    private final UUID id;

    @Setter
    private Instant createdAt;

    protected AggregateRoot(UUID id) {
        this.id = id;
    }
}
