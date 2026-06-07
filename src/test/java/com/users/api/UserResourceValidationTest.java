package com.users.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class UserResourceValidationTest {

    @Test
    void createUser_returns400_whenNameIsBlank() {
        Map<String, Object> request = Map.of(
                "name", " ",
                "email", "antonio@mail.com"
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void createUser_returns400_whenEmailIsInvalid() {
        Map<String, Object> request = Map.of(
                "name", "Anton",
                "email", "not-an-email"
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void createUser_returns400_whenPostalCodeExceedsMaxValue() {
        Map<String, Object> request = Map.of(
                "name", "Anton",
                "email", "antonio@mail.com",
                "postalCode", 100000000
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(400);
    }

    @Test
    void getUserById_returns404_whenUserDoesNotExist() {
        UUID randomId = UUID.randomUUID();

        given()
        .when()
            .get("/users/" + randomId)
        .then()
            .statusCode(404)
            .body("title", equalTo("User Not Found"))
            .body("status", equalTo(404));
    }
}