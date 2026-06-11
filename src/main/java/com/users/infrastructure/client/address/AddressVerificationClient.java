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

    // TODO: inject a Quarkus REST client pointing to the Address Verification Service.
    //       The service validates and normalizes the address, and resolves the postal code
    //       when missing. Address Verification Service is simulated — no real endpoint exists for this demo.

    @Override
    public AddressVerificationResult verify(User user) {
        Log.infof("Verifying address for user '%s': address='%s', postalCode=%s",
                user.getName(), user.getAddress(), user.getPostalCode());

        String verifiedAddress = user.getAddress();
        Integer resolvedPostalCode = user.getPostalCode();

        if (resolvedPostalCode == null) {
            resolvedPostalCode = 10000 + RANDOM.nextInt(90000);
            Log.infof("No postal code provided for user '%s' — resolved to %d from address",
                    user.getName(), resolvedPostalCode);
        }

        // TODO: POST /address/verify
        //       body: { "address": "<address>", "postalCode": <postalCode> }
        //       Expected response: { "address": "<normalized>", "postalCode": <resolved> }

        return new AddressVerificationResult(verifiedAddress, resolvedPostalCode);
    }
}
