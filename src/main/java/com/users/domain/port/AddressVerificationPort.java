package com.users.domain.port;

import com.users.domain.model.User;

public interface AddressVerificationPort {

    AddressVerificationResult verify(User user);
}
