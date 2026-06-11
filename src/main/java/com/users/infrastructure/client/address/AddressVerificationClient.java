package com.users.infrastructure.client.address;

import java.util.Random;

import com.users.domain.model.User;
import com.users.domain.port.AddressVerificationPort;
import com.users.domain.port.AddressVerificationResult;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressVerificationClient implements AddressVerificationPort {

    private static final Random RANDOM = new Random();

    // TODO replace this stub with a real REST client to the Address Verification Service
    //      (POST /address/verify -> normalised address + resolved postal code).
    @Override
    public AddressVerificationResult verify(User user) {
        Log.infof("Verifying address for user %s", user.getId());

        String verifiedAddress = user.getAddress();
        Integer resolvedPostalCode = user.getPostalCode();

        if (resolvedPostalCode == null) {
            resolvedPostalCode = 10000 + RANDOM.nextInt(90000);
            Log.infof("No postal code provided for user '%s' — resolved to %d from address",
                    user.getName(), resolvedPostalCode);
        }
        
        return new AddressVerificationResult(verifiedAddress, resolvedPostalCode);
    }
}
