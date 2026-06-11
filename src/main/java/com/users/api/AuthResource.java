package com.users.api;

import com.users.api.dto.LoginRequest;
import com.users.api.dto.TokenResponse;
import com.users.application.usecase.LoginUseCase;
import com.users.domain.port.security.TokenIssuer.IssuedToken;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    LoginUseCase loginUseCase;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginRequest request) {
        IssuedToken token = loginUseCase.execute(request.getEmail(), request.getPassword());
        return Response.ok(TokenResponse.bearer(token.token(), token.expiresInSeconds())).build();
    }
}
