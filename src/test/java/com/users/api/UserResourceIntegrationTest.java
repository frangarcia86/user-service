package com.users.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class UserResourceIntegrationTest {

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
    void getUserById_returns200_withUserData() {
        Map<String, Object> request = Map.of(
                "name", "Laura",
                "email", "laura.get@mail.com",
                "birthDate", "1990-03-15",
                "phone", "+34699887766",
                "address", "Madrid Avenue 7",
                "postalCode", 28001
        );

        String location = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().header("Location");

        String id = location.substring(location.lastIndexOf('/') + 1);

        given()
        .when()
            .get("/users/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Laura"))
            .body("email", equalTo("laura.get@mail.com"))
            .body("birthDate", equalTo("1990-03-15"))
            .body("phone", equalTo("+34699887766"))
            .body("address", equalTo("Madrid Avenue 7"))
            .body("postalCode", equalTo(28001))
            .body("createdAt", notNullValue());
    }
}