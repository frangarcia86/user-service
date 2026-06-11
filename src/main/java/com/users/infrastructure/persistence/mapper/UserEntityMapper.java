package com.users.infrastructure.persistence.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.users.domain.model.User;
import com.users.infrastructure.persistence.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UserEntityMapper {
    User toDomain(UserEntity entity);
    UserEntity toEntity(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(User user, @MappingTarget UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void replaceEntity(User user, @MappingTarget UserEntity entity);
}
