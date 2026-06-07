package com.users.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.api.dto.CreateUserRequest;
import com.users.api.dto.UserResponse;
import com.users.api.mapper.UserDtoMapper;
import com.users.application.usecase.CreateUserUseCase;
import com.users.application.usecase.GetUserByIdUseCase;
import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;

import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @Mock
    CreateUserUseCase createUserUseCase;

    @Mock
    GetUserByIdUseCase getUserByIdUseCase;

    @Mock
    UserDtoMapper userDtoMapper;

    @InjectMocks
    UserResource userResource;

    @Test
    void createUser_returns201WithLocationAndResponseBody() {
        // Arrange: request and domain input data
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        CreateUserRequest request = new CreateUserRequest();
        request.setName("Anton");
        request.setEmail("antonio@mail.com");
        request.setBirthDate(LocalDate.of(1986, 7, 20));
        request.setPhone("+34611223344");
        request.setAddress("Jaen Street 3");
        request.setPostalCode(29010);

        User domainRequest = new User(id, "Anton", "antonio@mail.com", createdAt);
        User createdUser = new User(id, "Anton", "antonio@mail.com", createdAt);
        createdUser.setBirthDate(LocalDate.of(1986, 7, 20));
        createdUser.setPhone("+34611223344");
        createdUser.setAddress("Jaen Street 3");
        createdUser.setPostalCode(29010);

        // Arrange: expected response payload
        UserResponse responseBody = UserResponse.builder()
                .id(id)
            .name("Anton")
            .email("antonio@mail.com")
            .birthDate(LocalDate.of(1986, 7, 20))
            .phone("+34611223344")
                .address("Jaen Street 3")
            .postalCode(29010)
                .createdAt(createdAt)
                .build();

        // Arrange: use case and mapper behavior
        when(userDtoMapper.toDomain(request)).thenReturn(domainRequest);
        when(createUserUseCase.execute(domainRequest)).thenReturn(createdUser);
        when(userDtoMapper.toResponse(createdUser)).thenReturn(responseBody);

        // Act: execute resource method
        Response response = userResource.createUser(request);

        // Assert: validate response and interactions
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getLocation()).isEqualTo(URI.create("/users/" + id));
        assertThat(response.getEntity()).isEqualTo(responseBody);

        verify(userDtoMapper).toDomain(request);
        verify(createUserUseCase).execute(domainRequest);
        verify(userDtoMapper).toResponse(createdUser);
    }

    @Test
    void getUserById_returns200WhenUserExists() {
        // Arrange: existing user data
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User(id, "Anton", "antonio@mail.com", createdAt);

        // Arrange: expected response payload
        UserResponse responseBody = UserResponse.builder()
                .id(id)
            .name("Anton")
            .email("antonio@mail.com")
                .createdAt(createdAt)
                .build();

        // Arrange: use case and mapper behavior
        when(getUserByIdUseCase.execute(id)).thenReturn(Optional.of(user));
        when(userDtoMapper.toResponse(user)).thenReturn(responseBody);

        // Act: execute resource method
        Response response = userResource.getUserById(id);

        // Assert: validate response and interactions
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo(responseBody);

        verify(getUserByIdUseCase).execute(id);
        verify(userDtoMapper).toResponse(user);
    }

    @Test
    void getUserById_throwsUserNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange: repository returns nothing for the requested id
        UUID id = UUID.randomUUID();

        when(getUserByIdUseCase.execute(id)).thenReturn(Optional.empty());

        // Act + Assert: resource throws domain exception
        assertThatThrownBy(() -> userResource.getUserById(id))
                .isInstanceOf(UserNotFoundException.class);

        verify(getUserByIdUseCase).execute(id);
    }
}
