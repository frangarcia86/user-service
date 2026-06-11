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
import com.users.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PartialUpdateUserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserUpdateMapper userUpdateMapper;

    @InjectMocks
    PartialUpdateUserUseCase patchUserUseCase;

    @Test
    void appliesOnlyProvidedFields() {
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Nuria Vidal", "nuria.vidal@web.es");
        existing.setPhone("+34633221100");
        existing.setAddress("Bilbao Lane 12");

        User saved = new User(id, "Nuria Vidal Updated", "nuria.vidal@web.es");
        saved.setPhone("+34633221100");
        saved.setAddress("Bilbao Lane 12");

        when(userRepository.findUserById(id)).thenReturn(Optional.of(existing));
        when(userRepository.update(existing)).thenReturn(saved);

        UserUpdateData data = new UserUpdateData("Nuria Vidal Updated", null, null, null, null);
        User result = patchUserUseCase.execute(id, data);

        assertThat(result).isEqualTo(saved);
        verify(userRepository).findUserById(id);
        verify(userUpdateMapper).applyPartialUpdate(data, existing);
        verify(userRepository).update(existing);
    }

    @Test
    void appliesAllFieldsWhenAllProvided() {
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Nuria Vidal", "nuria.vidal@web.es");

        User saved = new User(id, "Nuria Vidal Complete", "nuria.vidal@web.es");
        saved.setBirthDate(LocalDate.of(1987, 6, 14));
        saved.setPhone("+34633221100");
        saved.setAddress("Bilbao Lane 12");
        saved.setPostalCode(48001);

        when(userRepository.findUserById(id)).thenReturn(Optional.of(existing));
        when(userRepository.update(existing)).thenReturn(saved);

        UserUpdateData data = new UserUpdateData(
                "Nuria Vidal Complete",
                LocalDate.of(1987, 6, 14),
                "+34633221100",
                "Bilbao Lane 12",
                48001
        );
        User result = patchUserUseCase.execute(id, data);

        assertThat(result).isEqualTo(saved);
        verify(userRepository).findUserById(id);
        verify(userUpdateMapper).applyPartialUpdate(data, existing);
        verify(userRepository).update(existing);
    }

    @Test
    void throwsWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        UserUpdateData data = new UserUpdateData(null, null, null, null, null);

        when(userRepository.findUserById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patchUserUseCase.execute(id, data))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findUserById(id);
    }
}
