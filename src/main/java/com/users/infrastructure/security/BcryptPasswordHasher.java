package com.users.infrastructure.security;

import org.wildfly.security.password.interfaces.BCryptPassword;

import com.users.domain.port.security.PasswordHasher;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BcryptPasswordHasher implements PasswordHasher {

    private static final int COST = BCryptPassword.DEFAULT_ITERATION_COUNT;

    @Override
    public String hash(String rawPassword) {
        return BcryptUtil.bcryptHash(rawPassword, COST);
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash) {
        if (rawPassword == null || passwordHash == null) {
            return false;
        }
        return BcryptUtil.matches(rawPassword, passwordHash);
    }
}
