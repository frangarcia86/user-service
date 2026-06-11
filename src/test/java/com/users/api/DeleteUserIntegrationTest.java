package com.users.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
@TestSecurity(user = "test", roles = {"user", "admin"})
class DeleteUserIntegrationTest {

    @Test
    void deleteUser_returns204_whenUserExists() {
        Map<String, Object> createRequest = Map.of(
                "name", "Delete Me",
                "email", "delete.me@mail.com",
                "password", "sup3r-secret"
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

        given()
        .when()
            .delete("/users/" + id)
        .then()
            .statusCode(204);

        given()
        .when()
            .get("/users/" + id)
        .then()
            .statusCode(404)
            .body("title", equalTo("User Not Found"))
            .body("status", equalTo(404));
    }

    @Test
    void deleteUser_returns404_whenUserDoesNotExist() {
        UUID randomId = UUID.randomUUID();

        given()
        .when()
            .delete("/users/" + randomId)
        .then()
            .statusCode(404)
            .body("title", equalTo("User Not Found"))
            .body("status", equalTo(404));
    }
}
