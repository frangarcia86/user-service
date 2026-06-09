package com.users.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PatchUserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PatchUserUseCase patchUserUseCase;

    @Test
    void execute_appliesOnlyProvidedFields() {
        // Arrange: existing user with original data
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Anton", "antonio@mail.com");
        existing.setPhone("+34611000000");
        existing.setAddress("Old Street 1");

        User saved = new User(id, "Anton Updated", "antonio@mail.com");
        saved.setPhone("+34611000000");
        saved.setAddress("Old Street 1");

        when(userRepository.findUserById(id)).thenReturn(Optional.of(existing));
        when(userRepository.update(existing)).thenReturn(saved);

        // Act: only name is patched
        UserUpdateData data = new UserUpdateData("Anton Updated", null, null, null, null);
        User result = patchUserUseCase.execute(id, data);

        // Assert: only name changed on existing entity
        assertThat(existing.getName()).isEqualTo("Anton Updated");
        assertThat(existing.getPhone()).isEqualTo("+34611000000");
        assertThat(result).isEqualTo(saved);
        verify(userRepository).findUserById(id);
        verify(userRepository).update(existing);
    }

    @Test
    void execute_appliesAllFieldsWhenAllProvided() {
        // Arrange
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Anton", "antonio@mail.com");

        User saved = new User(id, "New Name", "antonio@mail.com");
        saved.setBirthDate(LocalDate.of(1995, 1, 1));
        saved.setPhone("+34699999999");
        saved.setAddress("New Address");
        saved.setPostalCode(10000);

        when(userRepository.findUserById(id)).thenReturn(Optional.of(existing));
        when(userRepository.update(existing)).thenReturn(saved);

        // Act
        UserUpdateData data = new UserUpdateData(
                "New Name",
                LocalDate.of(1995, 1, 1),
                "+34699999999",
                "New Address",
                10000
        );
        User result = patchUserUseCase.execute(id, data);

        // Assert
        assertThat(result).isEqualTo(saved);
        verify(userRepository).update(existing);
    }

    @Test
    void execute_throwsUserNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserUpdateData data = new UserUpdateData(null, null, null, null, null);

        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> patchUserUseCase.execute(id, data))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findUserById(id);
    }
}
