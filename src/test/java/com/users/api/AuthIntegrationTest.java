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
class AuthIntegrationTest {

    @Test
    void login_returns200_withBearerToken_whenCredentialsAreValid() {
        Map<String, Object> register = Map.of(
                "name", "Auth User",
                "email", "auth.ok@mail.com",
                "password", "sup3r-secret"
        );

        given()
            .contentType(ContentType.JSON)
            .body(register)
        .when()
            .post("/users")
        .then()
            .statusCode(201);

        Map<String, Object> login = Map.of(
                "email", "auth.ok@mail.com",
                "password", "sup3r-secret"
        );

        given()
            .contentType(ContentType.JSON)
            .body(login)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .body("accessToken", notNullValue())
            .body("tokenType", equalTo("Bearer"))
            .body("expiresIn", notNullValue());
    }

    @Test
    void login_returns401_whenPasswordIsWrong() {
        Map<String, Object> register = Map.of(
                "name", "Auth Bad",
                "email", "auth.bad@mail.com",
                "password", "sup3r-secret"
        );

        given()
            .contentType(ContentType.JSON)
            .body(register)
        .when()
            .post("/users")
        .then()
            .statusCode(201);

        Map<String, Object> login = Map.of(
                "email", "auth.bad@mail.com",
                "password", "wrong-password"
        );

        given()
            .contentType(ContentType.JSON)
            .body(login)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(401)
            .body("title", equalTo("Invalid Credentials"))
            .body("status", equalTo(401));
    }

    @Test
    void login_returns401_whenUserDoesNotExist() {
        Map<String, Object> login = Map.of(
                "email", "ghost@mail.com",
                "password", "any-pass"
        );

        given()
            .contentType(ContentType.JSON)
            .body(login)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(401);
    }

    @Test
    void protectedEndpoint_returns401_whenNoTokenIsProvided() {
        UUID anyId = UUID.randomUUID();

        given()
        .when()
            .get("/users/" + anyId)
        .then()
            .statusCode(401);
    }

    @Test
    void protectedEndpoint_returns200_withValidJwt() {
        Map<String, Object> register = Map.of(
                "name", "Token User",
                "email", "token.user@mail.com",
                "password", "sup3r-secret"
        );

        String location = given()
            .contentType(ContentType.JSON)
            .body(register)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().header("Location");

        String userId = location.substring(location.lastIndexOf('/') + 1);

        String token = given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", "token.user@mail.com", "password", "sup3r-secret"))
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .extract().path("accessToken");

        given()
            .auth().oauth2(token)
        .when()
            .get("/users/" + userId)
        .then()
            .statusCode(200)
            .body("email", equalTo("token.user@mail.com"));
    }

    @Test
    void adminOnlyEndpoint_returns403_whenUserHasOnlyUserRole() {
        Map<String, Object> register = Map.of(
                "name", "User Role",
                "email", "user.role@mail.com",
                "password", "sup3r-secret"
        );

        String location = given()
            .contentType(ContentType.JSON)
            .body(register)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .extract().header("Location");

        String userId = location.substring(location.lastIndexOf('/') + 1);

        String token = given()
            .contentType(ContentType.JSON)
            .body(Map.of("email", "user.role@mail.com", "password", "sup3r-secret"))
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .extract().path("accessToken");

        given()
            .auth().oauth2(token)
        .when()
            .delete("/users/" + userId)
        .then()
            .statusCode(403);
    }
}
