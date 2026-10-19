# wallet-service

A digital wallet REST service built with Java 21, Spring Boot 3, JPA/Hibernate, PostgreSQL and Flyway.
Features are added incrementally, and each one is driven by a real backend concern:
transactions, locking, N+1 queries, Kafka, Redis and security.

## Tech stack

- Java 21, Spring Boot 3.5
- Spring Data JPA / Hibernate
- PostgreSQL 16 (Docker), Flyway migrations
- JUnit 5

## Getting started

```bash
docker compose up -d postgres     # PostgreSQL 16 on localhost:5432
mvn test
mvn spring-boot:run
```

## API

### Open an account

```bash
curl -i -X POST localhost:8080/api/accounts \
  -H 'Content-Type: application/json' \
  -d '{"ownerName":"Alice","currency":"EUR","initialBalance":1000}'
```

### Get an account

```bash
curl -i localhost:8080/api/accounts/1
```

### Transfer money (work in progress)

```bash
curl -i -X POST localhost:8080/api/transfers \
  -H 'Content-Type: application/json' \
  -d '{"fromAccountId":1,"toAccountId":2,"amount":10}'
```

## Project structure

```
account/                  Account entity, repository, service and controller
transfer/                 Transfer service and controller
common/                   Exceptions and error responses (ProblemDetail)
db/migration/postgresql   Flyway migrations used by the application
db/migration/oracle       Oracle equivalent of the same schema, kept for comparison
```