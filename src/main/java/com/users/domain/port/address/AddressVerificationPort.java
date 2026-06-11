package com.users.domain.port.address;

import com.users.domain.model.User;

public interface AddressVerificationPort {

    AddressVerificationResult verify(User user);
}
