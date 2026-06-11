package com.users.infrastructure.client.notification.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessAlertRequest {

    private final UUID userId;
    private final String email;
    private final String name;
}
