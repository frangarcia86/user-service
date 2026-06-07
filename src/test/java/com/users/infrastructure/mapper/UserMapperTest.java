package com.users.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.users.domain.model.User;
import com.users.infrastructure.persistence.UserEntity;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_mapsAllUserFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User(id, "Fran", "fran@example.com", createdAt);
        user.setBirthDate(LocalDate.of(1990, 6, 15));
        user.setPhone("+34600000000");
        user.setAddress("Calle Mayor 1");
        user.setPostalCode(23740);

        UserEntity entity = mapper.toEntity(user);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Fran");
        assertThat(entity.getEmail()).isEqualTo("fran@example.com");
        assertThat(entity.getBirthDate()).isEqualTo(LocalDate.of(1990, 6, 15));
        assertThat(entity.getPhone()).isEqualTo("+34600000000");
        assertThat(entity.getAddress()).isEqualTo("Calle Mayor 1");
        assertThat(entity.getPostalCode()).isEqualTo(23740);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void toDomain_mapsAllEntityFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName("Fran");
        entity.setEmail("fran@example.com");
        entity.setBirthDate(LocalDate.of(1990, 6, 15));
        entity.setPhone("+34600000000");
        entity.setAddress("Calle Mayor 1");
        entity.setPostalCode(23740);
        entity.setCreatedAt(createdAt);

        User user = mapper.toDomain(entity);

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("Fran");
        assertThat(user.getEmail()).isEqualTo("fran@example.com");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1990, 6, 15));
        assertThat(user.getPhone()).isEqualTo("+34600000000");
        assertThat(user.getAddress()).isEqualTo("Calle Mayor 1");
        assertThat(user.getPostalCode()).isEqualTo(23740);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }
}
