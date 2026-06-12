package com.users.domain.port.address;

/**
 * Result of an address verification. Fields reflect the provider's normalized values,
 * which may differ from what the user originally submitted.
 */
public record AddressVerificationResult(String address, Integer postalCode) {}
