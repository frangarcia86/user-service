package com.users.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void create_shouldGenerateUUID() {
        User user = User.create("Anton", "antonio@mail.com");

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void create_shouldGenerateCreatedAt() {
        Instant before = Instant.now();
        User user = User.create("Anton", "antonio@mail.com");
        Instant after = Instant.now();

        assertThat(user.getCreatedAt())
                .isAfterOrEqualTo(before)
                .isBeforeOrEqualTo(after);
    }

    @Test
    void create_shouldSetNameAndEmail() {
        User user = User.create("Anton", "antonio@mail.com");

        assertThat(user.getName()).isEqualTo("Anton");
        assertThat(user.getEmail()).isEqualTo("antonio@mail.com");
    }

    @Test
    void create_shouldGenerateUniqueIds() {
        User user1 = User.create("Anton", "antonio@mail.com");
        User user2 = User.create("Anton", "antonio@mail.com");

        assertThat(user1.getId()).isNotEqualTo(user2.getId());
    }

    @Test
    void equals_shouldBeTrueForSameFields() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        User user1 = new User(id, "Anton", "antonio@mail.com", now);
        User user2 = new User(id, "Anton", "antonio@mail.com", now);

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void equals_shouldBeFalseForDifferentId() {
        Instant now = Instant.now();

        User user1 = new User(UUID.randomUUID(), "Anton", "antonio@mail.com", now);
        User user2 = new User(UUID.randomUUID(), "Anton", "antonio@mail.com", now);

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void hashCode_shouldBeEqualForSameFields() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        User user1 = new User(id, "Anton", "antonio@mail.com", now);
        User user2 = new User(id, "Anton", "antonio@mail.com", now);

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }
}
