package com.users.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.exception.EmailAlreadyExistsException;
import com.users.domain.model.User;
import com.users.domain.port.address.AddressVerificationPort;
import com.users.domain.port.address.AddressVerificationResult;
import com.users.domain.port.persistence.UserRepository;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AddressVerificationPort addressVerificationPort;

    @InjectMocks
    CreateUserUseCase createUserUseCase;

    @Test
    void execute_savesUserAndReturnsSavedUser() {
        UUID id = UUID.randomUUID();
        User input = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");
        User saved = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");

        when(userRepository.existsByEmail("raul.jimenez@correo.es")).thenReturn(false);
        when(userRepository.save(input)).thenReturn(saved);

        User result = createUserUseCase.execute(input);

        assertThat(result).isEqualTo(saved);
        verify(userRepository).save(input);
    }

    @Test
    void rejectsDuplicateEmail() {
        UUID id = UUID.randomUUID();
        User input = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");

        when(userRepository.existsByEmail("raul.jimenez@correo.es")).thenReturn(true);

        assertThatThrownBy(() -> createUserUseCase.execute(input))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void enrichesAddressData_whenAddressIsProvided() {
        UUID id = UUID.randomUUID();
        User input = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");
        input.setAddress("Calle Mayor 1");
        User saved = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");

        when(userRepository.existsByEmail("raul.jimenez@correo.es")).thenReturn(false);
        when(addressVerificationPort.verify(input))
                .thenReturn(new AddressVerificationResult("Calle Mayor 1", 28001));
        when(userRepository.save(input)).thenReturn(saved);

        createUserUseCase.execute(input);

        assertThat(input.getAddress()).isEqualTo("Calle Mayor 1");
        assertThat(input.getPostalCode()).isEqualTo(28001);
        verify(addressVerificationPort).verify(input);
    }

    @Test
    void skipsAddressVerification_whenAddressIsNull() {
        UUID id = UUID.randomUUID();
        User input = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");
        User saved = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");

        when(userRepository.existsByEmail("raul.jimenez@correo.es")).thenReturn(false);
        when(userRepository.save(input)).thenReturn(saved);

        createUserUseCase.execute(input);

        verify(addressVerificationPort, never()).verify(any());
    }
}

