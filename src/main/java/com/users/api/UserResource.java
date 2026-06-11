package com.users.api;

import java.net.URI;
import java.util.UUID;

import com.users.api.dto.CreateUserRequest;
import com.users.api.dto.PatchUserRequest;
import com.users.api.dto.UpdateUserRequest;
import com.users.api.dto.UserResponse;
import com.users.api.mapper.UserDtoMapper;
import com.users.application.usecase.CompleteUpdateUserUseCase;
import com.users.application.usecase.CreateUserUseCase;
import com.users.application.usecase.DeleteUserUseCase;
import com.users.application.usecase.GetUserByIdUseCase;
import com.users.application.usecase.PartialUpdateUserUseCase;
import com.users.domain.model.User;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    CreateUserUseCase createUserUseCase;

    @Inject
    GetUserByIdUseCase getUserByIdUseCase;

    @Inject
    CompleteUpdateUserUseCase updateUserUseCase;

    @Inject
    PartialUpdateUserUseCase patchUserUseCase;

    @Inject
    DeleteUserUseCase deleteUserUseCase;

    @Inject
    UserDtoMapper userDtoMapper;

    @POST
    @PermitAll
    public Response createUser(@Valid CreateUserRequest request) {
        User createdUser = createUserUseCase.execute(userDtoMapper.toDomain(request), request.getPassword());
        UserResponse response = userDtoMapper.toResponse(createdUser);

        return Response.created(URI.create("/users/" + createdUser.getId()))
                .entity(response)
                .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"user", "admin"})
    public Response getUserById(@PathParam("id") UUID id) {
        User user = getUserByIdUseCase.execute(id);
        return Response.ok(userDtoMapper.toResponse(user)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"user", "admin"})
    public Response updateUser(@PathParam("id") UUID id, @Valid UpdateUserRequest request) {
        User updatedUser = updateUserUseCase.execute(id, userDtoMapper.toUpdateData(request));
        return Response.ok(userDtoMapper.toResponse(updatedUser)).build();
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed({"user", "admin"})
    public Response patchUser(@PathParam("id") UUID id, @Valid PatchUserRequest request) {
        User patchedUser = patchUserUseCase.execute(id, userDtoMapper.toUpdateData(request));
        return Response.ok(userDtoMapper.toResponse(patchedUser)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteUser(@PathParam("id") UUID id) {
        deleteUserUseCase.execute(id);
        return Response.noContent().build();
    }
}
