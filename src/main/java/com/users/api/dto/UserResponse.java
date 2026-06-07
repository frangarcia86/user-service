package com.users.api.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private Instant createdAt;
}
