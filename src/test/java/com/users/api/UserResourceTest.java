package com.users.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import com.users.api.dto.PatchUserRequest;
import com.users.api.dto.UpdateUserRequest;
import com.users.api.dto.UserResponse;
import com.users.api.mapper.UserDtoMapper;
import com.users.application.usecase.CreateUserUseCase;
import com.users.application.usecase.DeleteUserUseCase;
import com.users.application.usecase.GetUserByIdUseCase;
import com.users.application.usecase.PatchUserUseCase;
import com.users.application.usecase.UpdateUserUseCase;
import com.users.application.usecase.UserUpdateData;
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
    UpdateUserUseCase updateUserUseCase;

    @Mock
    PatchUserUseCase patchUserUseCase;

    @Mock
    DeleteUserUseCase deleteUserUseCase;

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

        User domainRequest = new User(id, "Anton", "antonio@mail.com");
        User createdUser = new User(id, "Anton", "antonio@mail.com");
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

        User user = new User(id, "Anton", "antonio@mail.com");

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

    @Test
    void updateUser_returns200WithUpdatedResponseBody() {
        // Arrange
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Anton Updated");
        request.setBirthDate(LocalDate.of(1990, 5, 10));
        request.setPhone("+34611000001");
        request.setAddress("Updated Street 5");
        request.setPostalCode(28080);

        UserUpdateData updateData = new UserUpdateData(
                "Anton Updated", LocalDate.of(1990, 5, 10), "+34611000001", "Updated Street 5", 28080);
        User updatedUser = new User(id, "Anton Updated", "antonio@mail.com");
        updatedUser.setBirthDate(LocalDate.of(1990, 5, 10));

        UserResponse responseBody = UserResponse.builder()
                .id(id)
                .name("Anton Updated")
                .email("antonio@mail.com")
                .birthDate(LocalDate.of(1990, 5, 10))
                .phone("+34611000001")
                .address("Updated Street 5")
                .postalCode(28080)
                .createdAt(createdAt)
                .build();

        when(userDtoMapper.toUpdateData(request)).thenReturn(updateData);
        when(updateUserUseCase.execute(id, updateData)).thenReturn(updatedUser);
        when(userDtoMapper.toResponse(updatedUser)).thenReturn(responseBody);

        // Act
        Response response = userResource.updateUser(id, request);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo(responseBody);

        verify(userDtoMapper).toUpdateData(request);
        verify(updateUserUseCase).execute(id, updateData);
        verify(userDtoMapper).toResponse(updatedUser);
    }

    @Test
    void updateUser_throwsUserNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Anton Updated");

        UserUpdateData updateData = new UserUpdateData("Anton Updated", null, null, null, null);
        when(userDtoMapper.toUpdateData(request)).thenReturn(updateData);
        when(updateUserUseCase.execute(id, updateData)).thenThrow(new UserNotFoundException(id));

        // Act + Assert
        assertThatThrownBy(() -> userResource.updateUser(id, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void patchUser_returns200WithPatchedResponseBody() {
        // Arrange
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        PatchUserRequest request = new PatchUserRequest();
        request.setPhone("+34699000000");

        UserUpdateData patchData = new UserUpdateData(null, null, "+34699000000", null, null);
        User patchedUser = new User(id, "Anton", "antonio@mail.com");
        patchedUser.setPhone("+34699000000");

        UserResponse responseBody = UserResponse.builder()
                .id(id)
                .name("Anton")
                .email("antonio@mail.com")
                .phone("+34699000000")
                .createdAt(createdAt)
                .build();

        when(userDtoMapper.toUpdateData(request)).thenReturn(patchData);
        when(patchUserUseCase.execute(id, patchData)).thenReturn(patchedUser);
        when(userDtoMapper.toResponse(patchedUser)).thenReturn(responseBody);

        // Act
        Response response = userResource.patchUser(id, request);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo(responseBody);

        verify(userDtoMapper).toUpdateData(request);
        verify(patchUserUseCase).execute(id, patchData);
        verify(userDtoMapper).toResponse(patchedUser);
    }

    @Test
    void patchUser_throwsUserNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        PatchUserRequest request = new PatchUserRequest();
        request.setName("New Name");

        UserUpdateData patchData = new UserUpdateData("New Name", null, null, null, null);
        when(userDtoMapper.toUpdateData(request)).thenReturn(patchData);
        when(patchUserUseCase.execute(id, patchData)).thenThrow(new UserNotFoundException(id));

        // Act + Assert
        assertThatThrownBy(() -> userResource.patchUser(id, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUser_returns204WhenUserExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(deleteUserUseCase).execute(id);

        // Act
        Response response = userResource.deleteUser(id);

        // Assert
        assertThat(response.getStatus()).isEqualTo(204);
        verify(deleteUserUseCase).execute(id);
    }

    @Test
    void deleteUser_throwsUserNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new UserNotFoundException(id)).when(deleteUserUseCase).execute(id);

        // Act + Assert
        assertThatThrownBy(() -> userResource.deleteUser(id))
                .isInstanceOf(UserNotFoundException.class);

        verify(deleteUserUseCase).execute(id);
    }
}
