package com.users.api.mapper;

import java.time.Instant;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.users.api.dto.CreateUserRequest;
import com.users.api.dto.UserResponse;
import com.users.domain.model.User;

@Mapper(
		componentModel = MappingConstants.ComponentModel.CDI,
		imports = {UUID.class, Instant.class}
)
public interface UserDtoMapper {

	@Mapping(target = "id", expression = "java(UUID.randomUUID())")
	@Mapping(target = "createdAt", expression = "java(Instant.now())")
	User toDomain(CreateUserRequest request);

	UserResponse toResponse(User user);
}
