package com.users.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class CreateUserIntegrationTest {

    @Test
    void createUser_returns201_withCreatedUser() {
        Map<String, Object> request = Map.of(
                "name", "Anton",
                "email", "anton.happy@mail.com",
                "birthDate", "1986-07-20",
                "phone", "+34611223344",
                "address", "Jaen Street 3",
                "postalCode", 29010
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("Anton"))
            .body("email", equalTo("anton.happy@mail.com"))
            .body("birthDate", equalTo("1986-07-20"))
            .body("phone", equalTo("+34611223344"))
            .body("address", equalTo("Jaen Street 3"))
            .body("postalCode", equalTo(29010))
            .body("createdAt", notNullValue());
    }

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
            .statusCode(400)
            .body("title", equalTo("Constraint Violation"))
            .body("status", equalTo(400));
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
            .statusCode(400)
            .body("title", equalTo("Constraint Violation"))
            .body("status", equalTo(400));
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
            .statusCode(400)
            .body("title", equalTo("Constraint Violation"))
            .body("status", equalTo(400));
    }

    @Test
    void createUser_returns409_whenEmailAlreadyExists() {
        Map<String, Object> request = Map.of(
                "name", "Anton",
                "email", "duplicate.email@mail.com"
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(409)
            .body("title", equalTo("Email Already Exists"))
            .body("status", equalTo(409));
    }

    @Test
    void createUser_returns201_withGeneratedPostalCode_whenAddressProvidedButNoPostalCode() {
        Map<String, Object> request = Map.of(
                "name", "Carmen Lopez",
                "email", "carmen.lopez@mail.com",
                "address", "Calle Sierpes 10"
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .body("address", equalTo("Calle Sierpes 10"))
            .body("postalCode", allOf(greaterThanOrEqualTo(10000), lessThanOrEqualTo(99999)));
    }
}
