package com.users.domain.port.address;

import com.users.domain.model.User;

/**
 * Port for verifying and normalizing user addresses via an external provider.
 */
public interface AddressVerificationPort {

    /**
     * Verifies the address of the given user.
     *
     * @return normalized address data, or an empty result if verification fails
     */
    AddressVerificationResult verify(User user);
}
