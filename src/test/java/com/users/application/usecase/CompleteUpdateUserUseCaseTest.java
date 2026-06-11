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

import com.users.application.dto.UserUpdateData;
import com.users.application.mapper.UserUpdateMapper;
import com.users.domain.exception.UserNotFoundException;
import com.users.domain.model.User;
import com.users.domain.port.persistence.UserRepository;

@ExtendWith(MockitoExtension.class)
class CompleteUpdateUserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserUpdateMapper userUpdateMapper;

    @InjectMocks
    CompleteUpdateUserUseCase updateUserUseCase;

    @Test
    void updatesAllFieldsAndReturnsUpdatedUser() {
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Sofia Martin", "sofia.martin@empresa.com");

        UserUpdateData data = new UserUpdateData(
                "Sofia Martin Diaz",
                LocalDate.of(1993, 11, 3),
                "+34655443322",
                "Cordoba Plaza 5",
                14001
        );

        User saved = new User(id, "Sofia Martin Diaz", "sofia.martin@empresa.com");
        saved.setBirthDate(LocalDate.of(1993, 11, 3));
        saved.setPhone("+34655443322");
        saved.setAddress("Cordoba Plaza 5");
        saved.setPostalCode(14001);

        when(userRepository.findUserById(id)).thenReturn(Optional.of(existing));
        when(userRepository.replace(existing)).thenReturn(saved);

        User result = updateUserUseCase.execute(id, data);

        assertThat(result).isEqualTo(saved);
        verify(userRepository).findUserById(id);
        verify(userUpdateMapper).applyUpdate(data, existing);
        verify(userRepository).replace(existing);
    }

    @Test
    void throwsWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        UserUpdateData data = new UserUpdateData("Sofia Martin Diaz", null, null, null, null);

        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateUserUseCase.execute(id, data))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findUserById(id);
    }
}
