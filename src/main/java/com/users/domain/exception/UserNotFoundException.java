package com.users.domain.exception;

import java.util.UUID;

/** Thrown when no user exists with the given ID. */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID id) {
        super("User not found with id: " + id);
    }
}
