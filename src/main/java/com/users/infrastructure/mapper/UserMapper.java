package com.users.infrastructure.mapper;

import com.users.domain.model.User;
import com.users.infrastructure.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UserMapper {
    User toDomain(UserEntity entity);
    UserEntity toEntity(User user);
}
