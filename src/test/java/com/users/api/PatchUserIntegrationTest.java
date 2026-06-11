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
class PatchUserIntegrationTest {

    @Test
    void patchUser_returns200_withOnlyUpdatedFields() {
        Map<String, Object> createRequest = Map.of(
                "name", "Carlos Patch",
                "email", "carlos.patch@mail.com",
                "phone", "+34611000000",
                "address", "Seville Street 2",
                "postalCode", 41001
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

        Map<String, Object> patchRequest = Map.of("phone", "+34699000000");

        given()
            .contentType(ContentType.JSON)
            .body(patchRequest)
        .when()
            .patch("/users/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("name", equalTo("Carlos Patch"))
            .body("email", equalTo("carlos.patch@mail.com"))
            .body("phone", equalTo("+34699000000"))
            .body("address", equalTo("Seville Street 2"))
            .body("postalCode", equalTo(41001))
            .body("createdAt", notNullValue());
    }

    @Test
    void patchUser_returns200_withAllFieldsUpdated() {
        Map<String, Object> createRequest = Map.of(
                "name", "Maria Patch",
                "email", "maria.patch@mail.com"
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

        Map<String, Object> patchRequest = Map.of(
                "name", "Maria Updated",
                "birthDate", "1992-08-25",
                "phone", "+34622222222",
                "address", "Valencia Street 9",
                "postalCode", 46001
        );

        given()
            .contentType(ContentType.JSON)
            .body(patchRequest)
        .when()
            .patch("/users/" + id)
        .then()
            .statusCode(200)
            .body("name", equalTo("Maria Updated"))
            .body("birthDate", equalTo("1992-08-25"))
            .body("phone", equalTo("+34622222222"))
            .body("address", equalTo("Valencia Street 9"))
            .body("postalCode", equalTo(46001));
    }

    @Test
    void patchUser_returns404_whenUserDoesNotExist() {
        UUID randomId = UUID.randomUUID();

        Map<String, Object> patchRequest = Map.of("name", "New Name");

        given()
            .contentType(ContentType.JSON)
            .body(patchRequest)
        .when()
            .patch("/users/" + randomId)
        .then()
            .statusCode(404)
            .body("title", equalTo("User Not Found"))
            .body("status", equalTo(404));
    }
}
