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
import com.users.application.dto.UserUpdateData;
import com.users.application.usecase.CompleteUpdateUserUseCase;
import com.users.application.usecase.CreateUserUseCase;
import com.users.application.usecase.DeleteUserUseCase;
import com.users.application.usecase.GetUserByIdUseCase;
import com.users.application.usecase.PartialUpdateUserUseCase;
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
    CompleteUpdateUserUseCase updateUserUseCase;

    @Mock
    PartialUpdateUserUseCase patchUserUseCase;

    @Mock
    DeleteUserUseCase deleteUserUseCase;

    @Mock
    UserDtoMapper userDtoMapper;

    @InjectMocks
    UserResource userResource;

    @Test
    void createUser_returns201WithLocationAndResponseBody() {
        // Arrange
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        CreateUserRequest request = new CreateUserRequest();
        request.setName("Marta Sanchez");
        request.setEmail("marta.sanchez@mail.com");
        request.setPassword("sup3r-secret");
        request.setBirthDate(LocalDate.of(1988, 9, 12));
        request.setPhone("+34644332211");
        request.setAddress("Zaragoza Road 8");
        request.setPostalCode(50001);

        User domainRequest = new User(id, "Marta Sanchez", "marta.sanchez@mail.com");
        User createdUser = new User(id, "Marta Sanchez", "marta.sanchez@mail.com");
        createdUser.setBirthDate(LocalDate.of(1988, 9, 12));
        createdUser.setPhone("+34644332211");
        createdUser.setAddress("Zaragoza Road 8");
        createdUser.setPostalCode(50001);

        UserResponse responseBody = UserResponse.builder()
                .id(id)
                .name("Marta Sanchez")
                .email("marta.sanchez@mail.com")
                .birthDate(LocalDate.of(1988, 9, 12))
                .phone("+34644332211")
                .address("Zaragoza Road 8")
                .postalCode(50001)
                .createdAt(createdAt)
                .build();

        when(userDtoMapper.toDomain(request)).thenReturn(domainRequest);
        when(createUserUseCase.execute(domainRequest, "sup3r-secret")).thenReturn(createdUser);
        when(userDtoMapper.toResponse(createdUser)).thenReturn(responseBody);

        // Act
        Response response = userResource.createUser(request);

        // Assert
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getLocation()).isEqualTo(URI.create("/users/" + id));
        assertThat(response.getEntity()).isEqualTo(responseBody);

        verify(userDtoMapper).toDomain(request);
        verify(createUserUseCase).execute(domainRequest, "sup3r-secret");
        verify(userDtoMapper).toResponse(createdUser);
    }

    @Test
    void getUserById_returns200WhenUserExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User(id, "Pablo Navarro", "pablo.navarro@mail.com");

        UserResponse responseBody = UserResponse.builder()
                .id(id)
                .name("Pablo Navarro")
                .email("pablo.navarro@mail.com")
                .createdAt(createdAt)
                .build();

        when(getUserByIdUseCase.execute(id)).thenReturn(user);
        when(userDtoMapper.toResponse(user)).thenReturn(responseBody);

        // Act
        Response response = userResource.getUserById(id);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo(responseBody);

        verify(getUserByIdUseCase).execute(id);
        verify(userDtoMapper).toResponse(user);
    }

    @Test
    void getUserById_throwsUserNotFoundExceptionWhenUserDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(getUserByIdUseCase.execute(id)).thenThrow(new UserNotFoundException(id));

        // Act + Assert
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
        request.setName("Pablo Navarro Updated");
        request.setBirthDate(LocalDate.of(1985, 3, 21));
        request.setPhone("+34622334455");
        request.setAddress("Madrid Street 15");
        request.setPostalCode(28001);

        UserUpdateData updateData = new UserUpdateData(
                "Pablo Navarro Updated", LocalDate.of(1985, 3, 21), "+34622334455", "Madrid Street 15", 28001);
        User updatedUser = new User(id, "Pablo Navarro Updated", "pablo.navarro@mail.com");
        updatedUser.setBirthDate(LocalDate.of(1985, 3, 21));

        UserResponse responseBody = UserResponse.builder()
                .id(id)
                .name("Pablo Navarro Updated")
                .email("pablo.navarro@mail.com")
                .birthDate(LocalDate.of(1985, 3, 21))
                .phone("+34622334455")
                .address("Madrid Street 15")
                .postalCode(28001)
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
        UUID id = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Pablo Navarro Updated");

        UserUpdateData updateData = new UserUpdateData("Pablo Navarro Updated", null, null, null, null);
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
        request.setPhone("+34622334455");

        UserUpdateData patchData = new UserUpdateData(null, null, "+34622334455", null, null);
        User patchedUser = new User(id, "Pablo Navarro", "pablo.navarro@mail.com");
        patchedUser.setPhone("+34622334455");

        UserResponse responseBody = UserResponse.builder()
                .id(id)
                .name("Pablo Navarro")
                .email("pablo.navarro@mail.com")
                .phone("+34622334455")
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
        request.setName("Pablo Navarro Updated");

        UserUpdateData patchData = new UserUpdateData("Pablo Navarro Updated", null, null, null, null);
        when(userDtoMapper.toUpdateData(request)).thenReturn(patchData);
        when(patchUserUseCase.execute(id, patchData)).thenThrow(new UserNotFoundException(id));

        // Act + Assert
        assertThatThrownBy(() -> userResource.patchUser(id, request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUser_returns204WhenUserExists() {
        UUID id = UUID.randomUUID();
        doNothing().when(deleteUserUseCase).execute(id);

        Response response = userResource.deleteUser(id);

        assertThat(response.getStatus()).isEqualTo(204);
        verify(deleteUserUseCase).execute(id);
    }

    @Test
    void deleteUser_throwsUserNotFoundException_whenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        doThrow(new UserNotFoundException(id)).when(deleteUserUseCase).execute(id);

        assertThatThrownBy(() -> userResource.deleteUser(id))
                .isInstanceOf(UserNotFoundException.class);

        verify(deleteUserUseCase).execute(id);
    }
}
