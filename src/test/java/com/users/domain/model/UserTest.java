package com.users.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void constructor_shouldSetIdNameAndEmail() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Marta Sanchez", "marta.sanchez@mail.com");

        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    void constructor_shouldHaveNullCreatedAt() {
        User user = new User(UUID.randomUUID(), "Marta Sanchez", "marta.sanchez@mail.com");

        assertThat(user.getCreatedAt()).isNull();
    }

    @Test
    void constructor_shouldSetNameAndEmail() {
        User user = new User(UUID.randomUUID(), "Marta Sanchez", "marta.sanchez@mail.com");

        assertThat(user.getName()).isEqualTo("Marta Sanchez");
        assertThat(user.getEmail()).isEqualTo("marta.sanchez@mail.com");
    }

    @Test
    void constructor_shouldGenerateUniqueIdsWhenCalledTwice() {
        User user1 = new User(UUID.randomUUID(), "Marta Sanchez", "marta.sanchez@mail.com");
        User user2 = new User(UUID.randomUUID(), "Marta Sanchez", "marta.sanchez@mail.com");

        assertThat(user1.getId()).isNotEqualTo(user2.getId());
    }

    @Test
    void equals_shouldBeTrueForSameFields() {
        UUID id = UUID.randomUUID();

        User user1 = new User(id, "Marta Sanchez", "marta.sanchez@mail.com");
        User user2 = new User(id, "Marta Sanchez", "marta.sanchez@mail.com");

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void equals_shouldBeFalseForDifferentId() {
        User user1 = new User(UUID.randomUUID(), "Marta Sanchez", "marta.sanchez@mail.com");
        User user2 = new User(UUID.randomUUID(), "Marta Sanchez", "marta.sanchez@mail.com");

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void hashCode_shouldBeEqualForSameFields() {
        UUID id = UUID.randomUUID();

        User user1 = new User(id, "Marta Sanchez", "marta.sanchez@mail.com");
        User user2 = new User(id, "Marta Sanchez", "marta.sanchez@mail.com");

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }
}
