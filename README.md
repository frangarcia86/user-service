# user-service

Cloud-native microservice for user management. Built with Quarkus 3 on Java 25, PostgreSQL as the production datasource, H2 in-memory for tests.

## Requirements

- JDK 25+
- Docker (for the local PostgreSQL), or your own Postgres if you already have one running
- Maven wrapper is included, no global Maven install needed

## Quick start

Start PostgreSQL:

```shell
docker compose up -d
```

Run the service in dev mode (hot reload):

```shell
./mvnw quarkus:dev
```

The API is now available at <http://localhost:8080>. The Dev UI is at <http://localhost:8080/q/dev/>.

To stop the database when you're done: `docker compose down` (add `-v` to also drop the volume).

## API

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/auth/login` | public | Get a JWT for an existing user |
| `POST` | `/users` | public | Register a new user (returns the user, role `USER` is assigned) |
| `GET` | `/users/{id}` | `user`, `admin` | Get a user by ID |
| `PUT` | `/users/{id}` | `user`, `admin` | Replace a user (full update) |
| `PATCH` | `/users/{id}` | `user`, `admin` | Update some fields of a user |
| `DELETE` | `/users/{id}` | `admin` | Delete a user |

Full contract and try-it-out:

- Local Swagger UI: <http://localhost:8080/q/swagger-ui>
- Production Swagger UI: <https://user-service-1vnl.onrender.com/q/swagger-ui>

A Postman collection and environments live under [postman/](postman/) if you prefer that over Swagger.

## Authentication

Stateless JWT (RS256) issued and validated by this service.

1. `POST /users` to register (returns `201` and assigns the `USER` role).
2. `POST /auth/login` with `email` + `password` to get a `Bearer` token:

   ```json
   { "accessToken": "...", "tokenType": "Bearer", "expiresIn": 3600 }
   ```
3. Send it on protected calls: `Authorization: Bearer <token>`.

Roles:

- `USER` — read/update endpoints on `/users/{id}`.
- `ADMIN` — also allowed to `DELETE /users/{id}`. Currently no endpoint promotes a user to `ADMIN`; set the `role` column in the `credentials` table manually for testing.

Keys:

- `src/main/resources/privateKey.pem` (PKCS#8) signs tokens.
- `src/main/resources/publicKey.pem` (SubjectPublicKeyInfo) verifies them.
- The bundled pair is for local/dev only. In production override `mp.jwt.verify.publickey.location` and `smallrye.jwt.sign.key.location` (or mount the files via a secret) and set `JWT_ISSUER`.

| Property | Env var | Default |
|---|---|---|
| `mp.jwt.verify.issuer` / `smallrye.jwt.new-token.issuer` | `JWT_ISSUER` | `https://user-service.local` |
| `smallrye.jwt.new-token.lifespan` | `JWT_LIFESPAN_SECONDS` | `3600` |

## Health

| Environment | URL |
|---|---|
| Local | <http://localhost:8080/q/health> |
| Production | <https://user-service-1vnl.onrender.com/q/health> |

Returns `UP` when both the service and the database are reachable.

## Configuration

Business rules live in `src/main/resources/business.properties` and can be overridden at runtime via environment variables (no redeployment needed).

| Property | Env var | Default | Description |
|---|---|---|---|
| `user.access-alert.threshold-seconds` | `ACCESS_ALERT_THRESHOLD_SECONDS` | `60` | Seconds since user creation after which a `GET /users/{id}` triggers an access alert notification to the user's email. |

External REST clients live in `src/main/resources/clients.properties`.

| Property | Env var | Default | Description |
|---|---|---|---|
| `quarkus.rest-client.notification-service.url` | `NOTIFICATION_SERVICE_URL` | `http://localhost:8081` | Base URL of the Notification Service. See limitations below. |

Datasource and HTTP port (defined in `application.properties`):

| Property | Env var | Default |
|---|---|---|
| `quarkus.datasource.jdbc.url` | `DB_URL` | `jdbc:postgresql://localhost:5432/users` |
| `quarkus.datasource.username` | `DB_USER` | `postgres` |
| `quarkus.datasource.password` | `DB_PASSWORD` | `postgres` |
| `quarkus.http.port` | `PORT` | `8080` |

## Architecture

Hexagonal-ish layering with one inbound adapter (REST) and three outbound ports (persistence, notification, address verification):

```
api/                              REST adapter: DTOs, JAX-RS resource, exception mappers
application/                      Use cases (one per operation) + application-level DTOs/mappers
domain/
  model/                          Domain entities and aggregate roots
  exception/                      Domain exceptions
  port/
    address/                      AddressVerificationPort + result type
    notification/                 NotificationPort
    persistence/                  UserRepository
infrastructure/
  client/
    address/                      AddressVerificationClient
    notification/                 NotificationClient + REST DTO + REST proxy
  persistence/
    entity/                       JPA entities
    mapper/                       Entity <-> domain mappers (MapStruct)
    repository/                   Panache repository implementation
  security/                       BCrypt password hashing + JWT token issuer
```

Use cases depend only on the domain. Infrastructure implements the domain ports.

## Testing

```shell
./mvnw test
```

The test profile swaps PostgreSQL for an in-memory H2 (`%test.quarkus.datasource.*`), so you can run the suite without Docker.

What's covered:

- **Unit tests** for every use case and mapper (Mockito + AssertJ)
- **Integration tests** for each endpoint (`@QuarkusTest` + REST-Assured, hitting the full stack against H2)
- **Exception mapper tests** for the error response contract

## Packaging

Standard Quarkus jar:

```shell
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

Über-jar:

```shell
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

Native image (requires GraalVM, or container build):

```shell
./mvnw package -Dnative
# or, without GraalVM installed locally:
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Dockerfiles for each variant are under [docker/](docker/).

## Known limitations

This project is a demo/exercise, not a production-grade service. A few things are intentionally simplified:

- **Notification service** (`NotificationClient`) points to a non-existing endpoint. The call is fire-and-forget: a failure is logged as a warning but never propagates to the user. In production this should go through a queue or an outbox.
- **Address verification** (`AddressVerificationClient`) is a stub. It echoes the address back and, when the postal code is missing, generates a random one. Replace with a real REST client before using this anywhere serious.
- **No list/search endpoint** on `/users` yet. When the dataset grows we'll need pagination and filtering.
- **No role-admin endpoint**: the `USER` role is the only one assigned automatically on registration. Promoting a user to `ADMIN` requires updating the `credentials` table directly. A proper admin-management endpoint is pending.
- **H2 in tests, Postgres in prod**: the test profile uses H2, which has small SQL dialect differences vs Postgres. Integration tests against a real Postgres (Testcontainers) would catch them.
