package com.users.api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.users.api.dto.CreateUserRequest;
import com.users.api.dto.UserResponse;
import com.users.domain.model.User;

class UserDtoMapperTest {

    private final UserDtoMapper mapper = Mappers.getMapper(UserDtoMapper.class);

    @Test
    void toDomain_mapsRequestFields_andGeneratesIdAndCreatedAt() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Fran");
        request.setEmail("fran@example.com");
        request.setBirthDate(LocalDate.of(1990, 6, 15));
        request.setPhone("+34600000000");
        request.setAddress("Calle Mayor 1");
        request.setPostalCode(23740);

        Instant before = Instant.now();
        User user = mapper.toDomain(request);
        Instant after = Instant.now();

        assertThat(user.getId()).isNotNull();
        assertThat(user.getCreatedAt()).isAfterOrEqualTo(before).isBeforeOrEqualTo(after);
        assertThat(user.getName()).isEqualTo("Fran");
        assertThat(user.getEmail()).isEqualTo("fran@example.com");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1990, 6, 15));
        assertThat(user.getPhone()).isEqualTo("+34600000000");
        assertThat(user.getAddress()).isEqualTo("Calle Mayor 1");
        assertThat(user.getPostalCode()).isEqualTo(23740);
    }

    @Test
    void toResponse_mapsAllDomainFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User(id, "Fran", "fran@example.com", createdAt);
        user.setBirthDate(LocalDate.of(1990, 6, 15));
        user.setPhone("+34600000000");
        user.setAddress("Calle Mayor 1");
        user.setPostalCode(23740);

        UserResponse response = mapper.toResponse(user);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo("Fran");
        assertThat(response.getEmail()).isEqualTo("fran@example.com");
        assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(1990, 6, 15));
        assertThat(response.getPhone()).isEqualTo("+34600000000");
        assertThat(response.getAddress()).isEqualTo("Calle Mayor 1");
        assertThat(response.getPostalCode()).isEqualTo(23740);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }
}
