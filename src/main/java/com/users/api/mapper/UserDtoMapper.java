package com.users.api.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.users.api.dto.CreateUserRequest;
import com.users.api.dto.UserResponse;
import com.users.domain.model.User;

@Mapper(
		componentModel = MappingConstants.ComponentModel.CDI,
		imports = {UUID.class}
)
public interface UserDtoMapper {

	@Mapping(target = "id", expression = "java(UUID.randomUUID())")
	@Mapping(target = "createdAt", ignore = true)
	User toDomain(CreateUserRequest request);

	UserResponse toResponse(User user);
}
