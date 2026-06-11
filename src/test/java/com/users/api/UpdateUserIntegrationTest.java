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
class UpdateUserIntegrationTest {

    @Test
    void updateUser_returns200_withFullyUpdatedUser() {
        Map<String, Object> createRequest = Map.of(
                "name", "Laura Put",
                "email", "laura.put@mail.com",
                "birthDate", "1990-03-15",
                "phone", "+34699887766",
                "address", "Madrid Avenue 7",
                "postalCode", 28001
        );

        String location = given()
            .contentType(ContentType.JSON)
            .body(createRequest)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().header("Location");

        String id = location.substring(location.lastIndexOf('/') + 1);

        Map<String, Object> updateRequest = Map.of(
                "name", "Laura Updated",
                "birthDate", "1991-06-20",
                "phone", "+34611111111",
                "address", "Barcelona Street 10",
                "postalCode", 8001
        );

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
        .when()
            .put("/users/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Laura Updated"))
            .body("email", equalTo("laura.put@mail.com"))
            .body("birthDate", equalTo("1991-06-20"))
            .body("phone", equalTo("+34611111111"))
            .body("address", equalTo("Barcelona Street 10"))
            .body("postalCode", equalTo(8001))
            .body("createdAt", notNullValue());
    }

    @Test
    void updateUser_returns404_whenUserDoesNotExist() {
        UUID randomId = UUID.randomUUID();

        Map<String, Object> updateRequest = Map.of(
                "name", "Laura Updated"
        );

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
        .when()
            .put("/users/" + randomId)
        .then()
            .statusCode(404)
            .body("title", equalTo("User Not Found"))
            .body("status", equalTo(404));
    }

    @Test
    void updateUser_returns400_whenNameIsBlank() {
        Map<String, Object> createRequest = Map.of(
                "name", "Laura Put Validation",
                "email", "laura.put.validation@mail.com"
        );

        String location = given()
            .contentType(ContentType.JSON)
            .body(createRequest)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().header("Location");

        String id = location.substring(location.lastIndexOf('/') + 1);

        Map<String, Object> updateRequest = Map.of(
                "name", " "
        );

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
        .when()
            .put("/users/" + id)
        .then()
            .statusCode(400)
            .body("title", equalTo("Constraint Violation"))
            .body("status", equalTo(400));
    }
}
