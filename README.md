# Order Processing System

## Prerequisites

- Java 21
- Docker Desktop (running)

## 1. Start infrastructure (Postgres + Kafka)

From the repo root:

```
docker compose up -d
```

This starts:

| Service | Container | Port |
|---|---|---|
| Postgres | `order-processing-postgres` | `5432` |
| Kafka broker | `order-processing-kafka` | `9092` |
| Kafka UI | `order-processing-kafka-ui` | `8081` |

`kafka-init` only runs once to create the topics (`order.created`, `order.status-changed`) then exits — seeing it as `Exited (0)` is expected.

Check everything is up:

```
docker compose ps
```

`STATUS` for `postgres` and `kafka` should be `healthy`.

## 2. Run order-service

```
cd order-service
mvnw.cmd spring-boot:run
```

The app runs on `http://localhost:8080`. Flyway automatically applies migrations from `src/main/resources/db/migration` (including sample seed data).

## 3. URLs to remember

| Purpose | URL |
|---|---|
| REST API | `http://localhost:8080/products`, `http://localhost:8080/orders` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| Kafka UI | `http://localhost:8081` |
| Actuator health | `http://localhost:8080/actuator/health` |

## 4. Stop infrastructure

```
docker compose down
```

Data in Postgres/Kafka is preserved via volumes (`postgres-data`, `kafka-data`), so the next `docker compose up -d` will keep existing data. To wipe everything and start fresh:

```
docker compose down -v
```