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
    void toDomain_mapsRequestFields_andGeneratesId() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Alejandro Torres");
        request.setEmail("alejandro.torres@test.com");
        request.setBirthDate(LocalDate.of(1979, 11, 8));
        request.setPhone("+34677112233");
        request.setAddress("Valencia Street 9");
        request.setPostalCode(46001);

        User user = mapper.toDomain(request);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getName()).isEqualTo("Alejandro Torres");
        assertThat(user.getEmail()).isEqualTo("alejandro.torres@test.com");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1979, 11, 8));
        assertThat(user.getPhone()).isEqualTo("+34677112233");
        assertThat(user.getAddress()).isEqualTo("Valencia Street 9");
        assertThat(user.getPostalCode()).isEqualTo(46001);
    }

    @Test
    void toResponse_mapsAllDomainFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User(id, "Alejandro Torres", "alejandro.torres@test.com");
        user.setCreatedAt(createdAt);
        user.setBirthDate(LocalDate.of(1979, 11, 8));
        user.setPhone("+34677112233");
        user.setAddress("Valencia Street 9");
        user.setPostalCode(46001);

        UserResponse response = mapper.toResponse(user);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo("Alejandro Torres");
        assertThat(response.getEmail()).isEqualTo("alejandro.torres@test.com");
        assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(1979, 11, 8));
        assertThat(response.getPhone()).isEqualTo("+34677112233");
        assertThat(response.getAddress()).isEqualTo("Valencia Street 9");
        assertThat(response.getPostalCode()).isEqualTo(46001);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void toResponse_mapsNullCreatedAt_whenUserIsNew() {
        User user = new User(UUID.randomUUID(), "Alejandro Torres", "alejandro.torres@test.com");

        UserResponse response = mapper.toResponse(user);

        assertThat(response.getCreatedAt()).isNull();
    }
}
