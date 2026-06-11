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

## API

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/users` | Create a new user |
| `GET` | `/users/{id}` | Get a user by ID |
| `PUT` | `/users/{id}` | Replace a user (full update) |
| `PATCH` | `/users/{id}` | Update some fields of a user |
| `DELETE` | `/users/{id}` | Delete a user |

Full contract and try-it-out:

- Local Swagger UI: <http://localhost:8080/q/swagger-ui>
- Production Swagger UI: <https://user-service-1vnl.onrender.com/q/swagger-ui>

A Postman collection and environments live under [postman/](postman/) if you prefer that over Swagger.

## Health

| Environment | URL |
|---|---|
| Local | <http://localhost:8080/q/health> |
| Production | <https://user-service-1vnl.onrender.com/q/health> |

Returns `UP` when both the service and the database are reachable.

## Configuration

Business rules live in `src/main/resources/business.properties` and can be overridden at runtime via environment variables.

| Property | Env var | Default | Description |
|---|---|---|---|
| `user.access-alert.threshold-seconds` | `ACCESS_ALERT_THRESHOLD_SECONDS` | `60` | Seconds since user creation after which a `GET /users/{id}` triggers an access alert notification to the user's email. |

External REST clients live in `src/main/resources/clients.properties`.

| Property | Env var | Default | Description |
|---|---|---|---|
| `quarkus.rest-client.notification-service.url` | `NOTIFICATION_SERVICE_URL` | `http://localhost:8081` | Base URL of the Notification Service. See limitations below. |

Datasource and HTTP port (defined in `application.properties`):

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


## Known limitations

This project is a demo/exercise, a few things are intentionally simplified:

- **Notification service** (`NotificationClient`) points to a non-existing endpoint. The call is fire-and-forget: a failure is logged as a warning but never propagates to the user. In production this should go through a queue or an outbox.
- **Address verification** (`AddressVerificationClient`) is a stub. It echoes the address back and, when the postal code is missing, generates a random one. Replace with a real REST client before using this anywhere serious.
- **No authentication / authorization**. All endpoints are open.
- **No list/search endpoint** on `/users` yet.
- **Production database**: hosted on [Neon](https://neon.tech) (Postgres serverless, free tier).
- **Production hosting**: deployed on [Render](https://render.com) (free tier). The service idles after 15 minutes without traffic, so the first request after a quiet period can take around 50 seconds while it spins back up.
