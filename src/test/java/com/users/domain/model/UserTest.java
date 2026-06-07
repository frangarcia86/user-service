package com.users.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void create_shouldGenerateUUID() {
        User user = User.create("Fran", "fran@example.com");

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void create_shouldGenerateCreatedAt() {
        Instant before = Instant.now();
        User user = User.create("Fran", "fran@example.com");
        Instant after = Instant.now();

        assertThat(user.getCreatedAt())
                .isAfterOrEqualTo(before)
                .isBeforeOrEqualTo(after);
    }

    @Test
    void create_shouldSetNameAndEmail() {
        User user = User.create("Fran", "fran@example.com");

        assertThat(user.getName()).isEqualTo("Fran");
        assertThat(user.getEmail()).isEqualTo("fran@example.com");
    }

    @Test
    void create_shouldGenerateUniqueIds() {
        User user1 = User.create("Fran", "fran@example.com");
        User user2 = User.create("Fran", "fran@example.com");

        assertThat(user1.getId()).isNotEqualTo(user2.getId());
    }

    @Test
    void equals_shouldBeTrueForSameFields() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        User user1 = new User(id, "Fran", "fran@example.com", now);
        User user2 = new User(id, "Fran", "fran@example.com", now);

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void equals_shouldBeFalseForDifferentId() {
        Instant now = Instant.now();

        User user1 = new User(UUID.randomUUID(), "Fran", "fran@example.com", now);
        User user2 = new User(UUID.randomUUID(), "Fran", "fran@example.com", now);

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void hashCode_shouldBeEqualForSameFields() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        User user1 = new User(id, "Fran", "fran@example.com", now);
        User user2 = new User(id, "Fran", "fran@example.com", now);

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }
}
