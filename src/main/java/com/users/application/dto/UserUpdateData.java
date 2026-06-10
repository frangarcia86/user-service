package com.users.application.dto;

import java.time.LocalDate;

public record UserUpdateData(
        String name,
        LocalDate birthDate,
        String phone,
        String address,
        Integer postalCode
) {
}
