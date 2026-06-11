package com.users.infrastructure.security;

import java.util.Set;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.users.domain.model.Role;
import com.users.domain.port.security.TokenIssuer;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtTokenIssuer implements TokenIssuer {

    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan")
    long lifespanSeconds;

    @Override
    public IssuedToken issue(UUID userId, String email, Role role) {
        String token = Jwt.subject(userId.toString())
                .upn(email)
                .groups(Set.of(role.name().toLowerCase()))
                .claim("role", role.name())
                .sign();
        return new IssuedToken(token, lifespanSeconds);
    }
}
