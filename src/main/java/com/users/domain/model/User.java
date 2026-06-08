package com.users.domain.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User extends AggregateRoot {

    private final String email;
    
    @Setter
    private String name;

    @Setter
    private LocalDate birthDate;

    @Setter
    private String phone;

    @Setter
    private String address;

    @Setter
    private Integer postalCode;

    public User(UUID id, String name, String email) {
        super(id);
        this.name = name;
        this.email = email;
    }
}
