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
        request.setName("Anton");
        request.setEmail("antonio@mail.com");
        request.setBirthDate(LocalDate.of(1986, 7, 20));
        request.setPhone("+34611223344");
        request.setAddress("Jaen Street 3");
        request.setPostalCode(29010);

        Instant before = Instant.now();
        User user = mapper.toDomain(request);
        Instant after = Instant.now();

        assertThat(user.getId()).isNotNull();
        assertThat(user.getCreatedAt()).isAfterOrEqualTo(before).isBeforeOrEqualTo(after);
        assertThat(user.getName()).isEqualTo("Anton");
        assertThat(user.getEmail()).isEqualTo("antonio@mail.com");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1986, 7, 20));
        assertThat(user.getPhone()).isEqualTo("+34611223344");
        assertThat(user.getAddress()).isEqualTo("Jaen Street 3");
        assertThat(user.getPostalCode()).isEqualTo(29010);
    }

    @Test
    void toResponse_mapsAllDomainFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User(id, "Anton", "antonio@mail.com", createdAt);
        user.setBirthDate(LocalDate.of(1986, 7, 20));
        user.setPhone("+34611223344");
        user.setAddress("Jaen Street 3");
        user.setPostalCode(29010);

        UserResponse response = mapper.toResponse(user);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo("Anton");
        assertThat(response.getEmail()).isEqualTo("antonio@mail.com");
        assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(1986, 7, 20));
        assertThat(response.getPhone()).isEqualTo("+34611223344");
        assertThat(response.getAddress()).isEqualTo("Jaen Street 3");
        assertThat(response.getPostalCode()).isEqualTo(29010);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }
}
