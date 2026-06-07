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

        User user = new User(id, "Anton", "antonio@mail.com", createdAt);
        user.setBirthDate(LocalDate.of(1986, 7, 20));
        user.setPhone("+34611223344");
        user.setAddress("Jaen Street 3");
        user.setPostalCode(29010);

        UserEntity entity = mapper.toEntity(user);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Anton");
        assertThat(entity.getEmail()).isEqualTo("antonio@mail.com");
        assertThat(entity.getBirthDate()).isEqualTo(LocalDate.of(1986, 7, 20));
        assertThat(entity.getPhone()).isEqualTo("+34611223344");
        assertThat(entity.getAddress()).isEqualTo("Jaen Street 3");
        assertThat(entity.getPostalCode()).isEqualTo(29010);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void toDomain_mapsAllEntityFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName("Anton");
        entity.setEmail("antonio@mail.com");
        entity.setBirthDate(LocalDate.of(1986, 7, 20));
        entity.setPhone("+34611223344");
        entity.setAddress("Jaen Street 3");
        entity.setPostalCode(29010);
        entity.setCreatedAt(createdAt);

        User user = mapper.toDomain(entity);

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("Anton");
        assertThat(user.getEmail()).isEqualTo("antonio@mail.com");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1986, 7, 20));
        assertThat(user.getPhone()).isEqualTo("+34611223344");
        assertThat(user.getAddress()).isEqualTo("Jaen Street 3");
        assertThat(user.getPostalCode()).isEqualTo(29010);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }
}
