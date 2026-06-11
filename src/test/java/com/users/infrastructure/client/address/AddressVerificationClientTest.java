package com.users.infrastructure.client.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.users.domain.model.User;
import com.users.domain.port.address.AddressVerificationResult;

class AddressVerificationClientTest {

    private final AddressVerificationClient client = new AddressVerificationClient();

    @Test
    void verify_returnsProvidedAddressAndPostalCode_whenBothPresent() {
        User user = new User(UUID.randomUUID(), "Raul Jimenez", "raul.jimenez@correo.es");
        user.setAddress("Calle Mayor 1");
        user.setPostalCode(28001);

        AddressVerificationResult result = client.verify(user);

        assertThat(result.address()).isEqualTo("Calle Mayor 1");
        assertThat(result.postalCode()).isEqualTo(28001);
    }

    @Test
    void verify_generatesPostalCode_whenPostalCodeIsNull() {
        User user = new User(UUID.randomUUID(), "Raul Jimenez", "raul.jimenez@correo.es");
        user.setAddress("Calle Mayor 1");

        AddressVerificationResult result = client.verify(user);

        assertThat(result.address()).isEqualTo("Calle Mayor 1");
        assertThat(result.postalCode()).isBetween(10000, 99999);
    }

    @Test
    void verify_returnsNullAddress_whenAddressIsNull() {
        User user = new User(UUID.randomUUID(), "Raul Jimenez", "raul.jimenez@correo.es");
        user.setPostalCode(28001);

        AddressVerificationResult result = client.verify(user);

        assertThat(result.address()).isNull();
        assertThat(result.postalCode()).isEqualTo(28001);
    }
}
