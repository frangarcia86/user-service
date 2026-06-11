package com.users.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.users.domain.model.Credential;
import com.users.infrastructure.persistence.entity.CredentialEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface CredentialEntityMapper {
    Credential toDomain(CredentialEntity entity);
    CredentialEntity toEntity(Credential credential);
}
