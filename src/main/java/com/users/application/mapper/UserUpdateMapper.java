package com.users.application.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.users.application.dto.UserUpdateData;
import com.users.domain.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UserUpdateMapper {

    @Mapping(target = "createdAt", ignore = true)
    void applyUpdate(UserUpdateData data, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdAt", ignore = true)
    void applyPartialUpdate(UserUpdateData data, @MappingTarget User user);
}
