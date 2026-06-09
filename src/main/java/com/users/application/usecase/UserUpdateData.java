package com.users.application.usecase;

import java.time.LocalDate;

public record UserUpdateData(
        String name,
        LocalDate birthDate,
        String phone,
        String address,
        Integer postalCode
) {
}
