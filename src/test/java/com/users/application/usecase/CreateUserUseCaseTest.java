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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.users.domain.exception.EmailAlreadyExistsException;
import com.users.domain.model.Credential;
import com.users.domain.model.Role;
import com.users.domain.model.User;
import com.users.domain.port.address.AddressVerificationPort;
import com.users.domain.port.address.AddressVerificationResult;
import com.users.domain.port.persistence.CredentialRepository;
import com.users.domain.port.persistence.UserRepository;
import com.users.domain.port.security.PasswordHasher;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CredentialRepository credentialRepository;

    @Mock
    PasswordHasher passwordHasher;

    @Mock
    AddressVerificationPort addressVerificationPort;

    @InjectMocks
    CreateUserUseCase createUserUseCase;

    @Test
    void execute_savesUserAndCredentialAndReturnsSavedUser() {
        UUID id = UUID.randomUUID();
        User input = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");
        User saved = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");

        when(userRepository.existsByEmail("raul.jimenez@correo.es")).thenReturn(false);
        when(userRepository.save(input)).thenReturn(saved);
        when(passwordHasher.hash("s3cret-pass")).thenReturn("hashed");

        User result = createUserUseCase.execute(input, "s3cret-pass");

        assertThat(result).isEqualTo(saved);
        verify(userRepository).save(input);

        ArgumentCaptor<Credential> captor = ArgumentCaptor.forClass(Credential.class);
        verify(credentialRepository).save(captor.capture());
        Credential persisted = captor.getValue();
        assertThat(persisted.getUserId()).isEqualTo(id);
        assertThat(persisted.getPasswordHash()).isEqualTo("hashed");
        assertThat(persisted.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void rejectsDuplicateEmail() {
        UUID id = UUID.randomUUID();
        User input = new User(id, "Raul Jimenez", "raul.jimenez@correo.es");

        when(userRepository.existsByEmail("raul.jimenez@correo.es")).thenReturn(true);

        assertThatThrownBy(() -> createUserUseCase.execute(input, "s3cret-pass"))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(credentialRepository, never()).save(any());
        verify(passwordHasher, never()).hash(any());
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
        when(passwordHasher.hash("s3cret-pass")).thenReturn("hashed");

        createUserUseCase.execute(input, "s3cret-pass");

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
        when(passwordHasher.hash("s3cret-pass")).thenReturn("hashed");

        createUserUseCase.execute(input, "s3cret-pass");

        verify(addressVerificationPort, never()).verify(any());
    }
}
