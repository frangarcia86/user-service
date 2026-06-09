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
class UpdateUserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UpdateUserUseCase updateUserUseCase;

    @Test
    void execute_updatesAllFieldsAndReturnsUpdatedUser() {
        // Arrange: existing user in repository
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Anton", "antonio@mail.com");

        UserUpdateData data = new UserUpdateData(
                "Antonio Updated",
                LocalDate.of(1990, 5, 10),
                "+34611000000",
                "New Street 1",
                28080
        );

        User saved = new User(id, "Antonio Updated", "antonio@mail.com");
        saved.setBirthDate(LocalDate.of(1990, 5, 10));
        saved.setPhone("+34611000000");
        saved.setAddress("New Street 1");
        saved.setPostalCode(28080);

        when(userRepository.findUserById(id)).thenReturn(Optional.of(existing));
        when(userRepository.update(existing)).thenReturn(saved);

        // Act
        User result = updateUserUseCase.execute(id, data);

        // Assert
        assertThat(result).isEqualTo(saved);
        verify(userRepository).findUserById(id);
        verify(userRepository).update(existing);
    }

    @Test
    void execute_throwsUserNotFoundException_whenUserDoesNotExist() {
        // Arrange: repository returns empty
        UUID id = UUID.randomUUID();
        UserUpdateData data = new UserUpdateData("Antonio Updated", null, null, null, null);

        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> updateUserUseCase.execute(id, data))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findUserById(id);
    }
}
