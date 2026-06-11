package com.users.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.users.domain.model.User;
import com.users.infrastructure.persistence.UserEntity;

class UserEntityMapperTest {

    private final UserEntityMapper mapper = Mappers.getMapper(UserEntityMapper.class);

    @Test
    void toEntity_mapsAllUserFields() {
        UUID id = UUID.randomUUID();

        User user = new User(id, "Gonzalo Reyes", "gonzalo.reyes@correo.net");
        user.setBirthDate(LocalDate.of(1982, 4, 3));
        user.setPhone("+34644221100");
        user.setAddress("Seville Avenue 3");
        user.setPostalCode(41001);

        UserEntity entity = mapper.toEntity(user);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Gonzalo Reyes");
        assertThat(entity.getEmail()).isEqualTo("gonzalo.reyes@correo.net");
        assertThat(entity.getBirthDate()).isEqualTo(LocalDate.of(1982, 4, 3));
        assertThat(entity.getPhone()).isEqualTo("+34644221100");
        assertThat(entity.getAddress()).isEqualTo("Seville Avenue 3");
        assertThat(entity.getPostalCode()).isEqualTo(41001);
        assertThat(entity.getCreatedAt()).isNull();
    }

    @Test
    void toDomain_mapsAllEntityFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName("Gonzalo Reyes");
        entity.setEmail("gonzalo.reyes@correo.net");
        entity.setBirthDate(LocalDate.of(1982, 4, 3));
        entity.setPhone("+34644221100");
        entity.setAddress("Seville Avenue 3");
        entity.setPostalCode(41001);
        entity.setCreatedAt(createdAt);

        User user = mapper.toDomain(entity);

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("Gonzalo Reyes");
        assertThat(user.getEmail()).isEqualTo("gonzalo.reyes@correo.net");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1982, 4, 3));
        assertThat(user.getPhone()).isEqualTo("+34644221100");
        assertThat(user.getAddress()).isEqualTo("Seville Avenue 3");
        assertThat(user.getPostalCode()).isEqualTo(41001);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void updateEntity_updatesOnlyMappedFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setEmail("gonzalo.reyes@correo.net");
        entity.setCreatedAt(createdAt);
        entity.setName("Gonzalo Reyes");

        User user = new User(id, "Gonzalo Reyes Updated", "gonzalo.reyes@correo.net");
        user.setBirthDate(LocalDate.of(1982, 4, 3));
        user.setPhone("+34644221100");

        mapper.updateEntity(user, entity);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getEmail()).isEqualTo("gonzalo.reyes@correo.net");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getName()).isEqualTo("Gonzalo Reyes Updated");
        assertThat(entity.getBirthDate()).isEqualTo(LocalDate.of(1982, 4, 3));
        assertThat(entity.getPhone()).isEqualTo("+34644221100");
    }
}
