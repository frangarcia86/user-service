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
        User existing = new User(id, "Nuria Vidal", "nuria.vidal@web.es");
        existing.setPhone("+34633221100");
        existing.setAddress("Bilbao Lane 12");

        User saved = new User(id, "Nuria Vidal Updated", "nuria.vidal@web.es");
        saved.setPhone("+34633221100");
        saved.setAddress("Bilbao Lane 12");

        when(userRepository.findUserById(id)).thenReturn(Optional.of(existing));
        when(userRepository.update(existing)).thenReturn(saved);

        // Act: only name is patched
        UserUpdateData data = new UserUpdateData("Nuria Vidal Updated", null, null, null, null);
        User result = patchUserUseCase.execute(id, data);

        // Assert: only name changed on existing entity
        assertThat(existing.getName()).isEqualTo("Nuria Vidal Updated");
        assertThat(existing.getPhone()).isEqualTo("+34633221100");
        assertThat(result).isEqualTo(saved);
        verify(userRepository).findUserById(id);
        verify(userRepository).update(existing);
    }

    @Test
    void execute_appliesAllFieldsWhenAllProvided() {
        // Arrange
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Nuria Vidal", "nuria.vidal@web.es");

        User saved = new User(id, "Nuria Vidal Complete", "nuria.vidal@web.es");
        saved.setBirthDate(LocalDate.of(1987, 6, 14));
        saved.setPhone("+34633221100");
        saved.setAddress("Bilbao Lane 12");
        saved.setPostalCode(48001);

        when(userRepository.findUserById(id)).thenReturn(Optional.of(existing));
        when(userRepository.update(existing)).thenReturn(saved);

        // Act
        UserUpdateData data = new UserUpdateData(
                "Nuria Vidal Complete",
                LocalDate.of(1987, 6, 14),
                "+34633221100",
                "Bilbao Lane 12",
                48001
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
