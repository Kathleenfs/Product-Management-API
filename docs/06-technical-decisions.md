# Technical Decisions

## Overview

This document records the main technical decisions adopted in the Product Management API and the reasoning behind them.

The decisions prioritize maintainability, separation of concerns, security, testability, data integrity, and reproducible development environments.

---

## Java 21

Java 21 was selected as the language version used by the application.

As an LTS release, it provides long-term support while allowing the project to use a modern Java ecosystem.

---

## Spring Boot

Spring Boot was selected as the application framework to simplify configuration and provide integration with the Spring ecosystem.

The project uses Spring Boot together with:

- Spring MVC
- Spring Data JPA
- Spring Security
- Bean Validation

This provides a consistent foundation for REST API development, persistence, validation, and security.

---

## Layered Architecture

The application uses a Layered Architecture.

The main responsibilities are separated into:

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
Database
```

Supporting responsibilities are separated into DTOs, mappers, security components, configuration, and exception handling.

This architecture was selected to provide:

- Clear separation of concerns
- Explicit responsibilities
- Easier maintenance
- Testable business logic
- Reduced coupling between HTTP and persistence concerns

The application remains a single Spring Boot application rather than being divided into microservices.

---

## DTOs Instead of Exposing Entities

Persistence entities are not exposed directly through the REST API.

The application uses separate request and response DTOs:

```text
Request DTO
     |
     v
   Mapper
     |
     v
   Entity
```

and:

```text
Entity
   |
   v
 Mapper
   |
   v
Response DTO
```

This decision separates the external API contract from the persistence model.

It also prevents internal entity changes from automatically changing the public API representation.

---

## Dedicated Mappers

Mapping logic is centralized in dedicated mapper components rather than being duplicated inside controllers or services.

Mappers are responsible for conversions such as:

- Request DTO → Entity
- Entity → Response DTO

This keeps transformation logic separated from business rules and reduces duplicated mapping code.

---

## PostgreSQL

PostgreSQL was selected as the relational database.

The application contains relational concepts such as:

```text
Category 1 -------- N Product

User N -------- N Role
```

A relational database provides appropriate support for these relationships, constraints, and transactional persistence requirements.

---

## Flyway

Flyway manages database schema versioning through SQL migration scripts.

Database changes are explicit and version controlled.

Migration files are stored under:

```text
src/main/resources/db/migration
```

Using Flyway provides:

- Reproducible schema creation
- Version-controlled database changes
- Traceable schema evolution
- Consistent database structures between environments
- Reduced reliance on manual database changes

---

## Flyway as the Schema Source of Truth

Database schema creation and evolution are controlled by Flyway rather than Hibernate.

Hibernate is configured with:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

The responsibility is therefore separated as:

```text
Flyway
   |
   | creates / evolves
   v
Database Schema
   ^
   | validates
   |
Hibernate
```

Hibernate validates whether entity mappings match the database schema but does not create or modify it automatically.

This prevents implicit schema modifications during application startup and keeps database evolution explicit and versioned.

---

## Database Constraints

Database constraints are used as a final layer of data integrity.

Business validations are performed in the application layer, while database constraints protect persisted data independently.

Examples include:

- Primary keys
- Foreign keys
- Unique constraints
- Required columns

This provides two levels of protection:

```text
Application Validation
        +
Database Constraints
```

Application validation provides controlled business responses, while database constraints preserve structural data integrity.

---

## Spring Data JPA

Spring Data JPA was selected as the persistence abstraction.

Repositories provide standard persistence operations while derived query methods are used for additional application requirements.

Example:

```java
existsByName(String name)
```

This reduces persistence boilerplate while keeping database access isolated from business logic.

Hibernate is used as the JPA implementation responsible for ORM and entity persistence.

---

## Logical Deactivation

Products and categories use an `active` state rather than relying exclusively on physical deletion.

For example:

```http
DELETE /products/{id}
```

results in logical deactivation:

```text
active = true
     |
     | DELETE
     v
active = false
```

The record remains persisted in the database.

Resources can subsequently be reactivated through dedicated endpoints.

This approach preserves persisted records while allowing the application to control whether a resource is currently active.

---

## JWT Authentication

JWT was selected for stateless authentication.

After successful login:

```text
Credentials
    |
    v
Authentication
    |
    v
JWT Generated
```

Protected requests send:

```text
Authorization: Bearer <JWT>
```

The server does not maintain authenticated HTTP sessions.

This keeps authentication stateless and allows each protected request to contain the information required for authentication.

---

## Role-Based Access Control

Authorization is implemented using roles.

The application defines:

```text
ROLE_USER
ROLE_ADMIN
```

The authorization model is:

| Role | Read Operations | Write Operations |
|---|---|---|
| `ROLE_USER` | Allowed | Forbidden |
| `ROLE_ADMIN` | Allowed | Allowed |

This creates a clear separation between authentication and authorization:

```text
Authentication
"Who is the user?"

        |
        v

Authorization
"What can the user do?"
```

---

## BCrypt Password Hashing

User passwords are not stored as plain text.

Spring Security's `PasswordEncoder` with BCrypt is used to hash passwords before persistence and verify credentials during authentication.

The database therefore stores password hashes instead of the original passwords.

---

## Externalized JWT Configuration

JWT configuration is externalized instead of being hardcoded in the source code.

The signing secret is provided through:

```text
JWT_SECRET
```

Token expiration can optionally be configured through:

```text
JWT_EXPIRATION
```

with a default value of:

```text
3600000 ms
```

Application configuration:

```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:3600000}
```

This separates sensitive configuration from application source code and allows security configuration to vary between environments.

---

## Spring Profiles

Spring Profiles are used to separate environment-specific application configuration.

The base configuration defines the application name and active development profile:

```yaml
spring:
  application:
    name: product-management-api

  profiles:
    active: dev
```

Environment-specific configuration can then override properties according to the execution environment.

The development configuration contains settings such as:

- Local PostgreSQL connection
- Flyway migrations
- Hibernate schema validation
- SQL logging
- JWT configuration

For development, SQL output can be enabled:

```yaml
spring:
  jpa:
    show-sql: true
```

Other environment configurations can override this behavior:

```yaml
spring:
  jpa:
    show-sql: false
```

Using Spring Profiles keeps environment-specific configuration separated from business logic and allows the same application to run with different settings.

---

## Bean Validation

Request validation is performed using Jakarta Bean Validation.

Validation rules are declared in request DTOs and activated through `@Valid` in controllers.

Examples include annotations such as:

```text
@NotBlank
@NotNull
@Size
@Min
@DecimalMin
```

This creates a distinction between:

```text
DTO Validation
     |
     v
Business Validation
```

DTO validation protects the API contract, while Service-layer validation protects business rules and application state.

---

## Centralized Exception Handling

Exception handling is centralized through `GlobalExceptionHandler`.

Instead of duplicating error-handling logic across controllers:

```text
Controller / Service
        |
        v
Exception
        |
        v
GlobalExceptionHandler
        |
        v
Standardized HTTP Response
```

This provides consistent error responses while keeping controllers focused on HTTP operations.

Handled scenarios include validation errors, missing resources, duplicate resources, authentication failures, and invalid application states.

---

## Automated Testing

Automated tests focus primarily on the Service layer, where the main business rules are implemented.

JUnit 5 is used as the testing framework and Mockito is used to isolate dependencies.

Conceptually:

```text
Service Under Test
      |
      +---- Mock Repository
      |
      +---- Mock Mapper
      |
      +---- Mock Dependencies
```

The test suite covers both successful and failure scenarios.

Examples include:

- Resource creation
- Resource retrieval
- Updates
- Activation
- Deactivation
- Resource not found
- Duplicate resources
- Invalid credentials
- Inactive users
- JWT generation
- JWT validation

This allows business behavior to be tested without requiring a running PostgreSQL instance.

---

## MockMvc for Web Layer Testing

Selected controller behavior is tested using MockMvc.

MockMvc allows HTTP requests and responses to be tested without starting an external web server.

The project uses these tests selectively to validate behavior such as:

- HTTP status codes
- Request validation
- Successful controller requests

The majority of business-rule coverage remains in Service tests.

---

## JaCoCo

JaCoCo is used to measure automated test coverage.

The coverage report provides information about:

- Instructions
- Branches
- Lines
- Methods
- Classes

The HTML report is generated at:

```text
target/site/jacoco/index.html
```

Coverage is used as a diagnostic tool to identify untested execution paths rather than treating a specific percentage as the sole measure of test quality.

---

## OpenAPI and Swagger UI

OpenAPI is used to describe the REST API, while Swagger UI provides interactive documentation.

Swagger UI allows developers to:

- Inspect available endpoints
- View request schemas
- View response schemas
- Execute HTTP requests
- Test protected endpoints

Swagger UI is configured with JWT Bearer authentication.

The authentication flow can therefore be tested directly from the documentation:

```text
/auth/login
     |
     v
JWT
     |
     v
Swagger Authorize
     |
     v
Protected Endpoint
```

---

## Docker Compose

Docker Compose is used to provide a reproducible PostgreSQL development environment.

The local development flow is:

```text
Spring Boot Application
        |
        v
localhost:5433
        |
        v
PostgreSQL Container
```

Containerizing PostgreSQL avoids requiring a matching local PostgreSQL installation and configuration.

The Spring Boot application itself can run through Maven during local development.

---

## Maven

Maven manages project dependencies, build execution, automated tests, coverage integration, and application packaging.

The standard build command is:

```bash
mvn clean install
```

The Maven lifecycle provides a consistent process for compiling, testing, and packaging the application.

---

## Configuration Strategy

Application configuration is divided between:

```text
Base Configuration
        |
        v
Spring Profile
        |
        v
Environment Variables
```

The base configuration defines general application behavior.

Spring Profiles separate environment-specific properties.

Environment variables provide sensitive or externally configurable values such as JWT secrets.

This prevents business logic from depending directly on environment-specific configuration.

---

## Technical Decision Summary

| Decision | Technology / Approach |
|---|---|
| Language | Java 21 |
| Application Framework | Spring Boot |
| Architecture | Layered Architecture |
| API Style | REST |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Schema Versioning | Flyway |
| Schema Management | Flyway + Hibernate `validate` |
| API Contracts | Request / Response DTOs |
| Object Conversion | Dedicated Mappers |
| Authentication | JWT |
| Authorization | RBAC |
| Password Storage | BCrypt |
| Resource Removal | Logical Deactivation |
| Validation | Jakarta Bean Validation |
| Error Handling | Global Exception Handler |
| Environment Configuration | Spring Profiles |
| Sensitive Configuration | Environment Variables |
| Unit Testing | JUnit 5 |
| Dependency Mocking | Mockito |
| Web Layer Testing | MockMvc |
| Coverage | JaCoCo |
| API Documentation | OpenAPI / Swagger UI |
| Local Database Environment | Docker Compose |
| Build | Maven |

---

## Version 1.0.0

These decisions represent the technical foundation of version `1.0.0`.

The current version focuses on a single Spring Boot application using Layered Architecture.

Capabilities such as:

- Pagination and sorting
- Dynamic filtering
- Redis caching
- Metrics and observability
- CI/CD
- Cloud deployment
- Microservices
- Alternative architectural styles

are intentionally outside the scope of the current version and can be explored independently without changing the core architectural responsibilities established by this application.