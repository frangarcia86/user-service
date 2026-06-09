package com.users.api;

import java.net.URI;
import java.util.UUID;

import com.users.api.dto.CreateUserRequest;
import com.users.api.dto.PatchUserRequest;
import com.users.api.dto.UpdateUserRequest;
import com.users.api.dto.UserResponse;
import com.users.api.mapper.UserDtoMapper;
import com.users.application.usecase.CreateUserUseCase;
import com.users.application.usecase.DeleteUserUseCase;
import com.users.application.usecase.GetUserByIdUseCase;
import com.users.application.usecase.PatchUserUseCase;
import com.users.application.usecase.UpdateUserUseCase;
import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;

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
	UpdateUserUseCase updateUserUseCase;

	@Inject
	PatchUserUseCase patchUserUseCase;

	@Inject
	DeleteUserUseCase deleteUserUseCase;

	@Inject
	UserDtoMapper userDtoMapper;

	@POST
	public Response createUser(@Valid CreateUserRequest request) {
		User createdUser = createUserUseCase.execute(userDtoMapper.toDomain(request));
		UserResponse response = userDtoMapper.toResponse(createdUser);

		return Response.created(URI.create("/users/" + createdUser.getId()))
				.entity(response)
				.build();
	}

	@GET
	@Path("/{id}")
	public Response getUserById(@PathParam("id") UUID id) {
		User user = getUserByIdUseCase.execute(id)
				.orElseThrow(() -> new UserNotFoundException(id));

		UserResponse response = userDtoMapper.toResponse(user);
		return Response.ok(response).build();
	}

	@PUT
	@Path("/{id}")
	public Response updateUser(@PathParam("id") UUID id, @Valid UpdateUserRequest request) {
		User updatedUser = updateUserUseCase.execute(id, userDtoMapper.toUpdateData(request));
		UserResponse response = userDtoMapper.toResponse(updatedUser);
		return Response.ok(response).build();
	}

	@PATCH
	@Path("/{id}")
	public Response patchUser(@PathParam("id") UUID id, @Valid PatchUserRequest request) {
		User patchedUser = patchUserUseCase.execute(id, userDtoMapper.toUpdateData(request));
		UserResponse response = userDtoMapper.toResponse(patchedUser);
		return Response.ok(response).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") UUID id) {
		deleteUserUseCase.execute(id);
		return Response.noContent().build();
	}
}
