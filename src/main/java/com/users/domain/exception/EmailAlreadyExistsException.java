package com.users.domain.exception;

/** Thrown when trying to register an email that is already in use. */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}
