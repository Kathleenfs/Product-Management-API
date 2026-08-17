# Architecture

## Overview

The Product Management API follows a **Layered Architecture**, where responsibilities are separated across application layers.

This approach keeps HTTP handling, business rules, object mapping, persistence, security, and error handling isolated, improving maintainability, readability, and testability.

The application is implemented as a single Spring Boot application and follows a unidirectional dependency flow between its main layers.

---

## Architecture Diagram

```text
                        HTTP Request
                             |
                             v
                    +------------------+
                    | Spring Security  |
                    | JWT Filter       |
                    +--------+---------+
                             |
                             v
                    +------------------+
                    |    Controller    |
                    +--------+---------+
                             |
                       Request DTO
                             |
                             v
                    +------------------+
                    |     Service      |
                    +--------+---------+
                             |
                    +--------+---------+
                    |                  |
                    v                  v
              +-----------+      +------------+
              |  Mapper   |      | Repository |
              +-----------+      +------+-----+
                                       |
                                       v
                                 +------------+
                                 | PostgreSQL |
                                 +------------+
```

Response flow:

```text
PostgreSQL
     |
     v
Repository
     |
     v
Entity
     |
     v
Service
     |
     v
Mapper
     |
     v
Response DTO
     |
     v
Controller
     |
     v
HTTP Response
```

---

## Layers

### Controller

Controllers expose the REST API and act as the entry point for HTTP requests.

Responsibilities include:

- Receiving HTTP requests.
- Mapping endpoint parameters and request bodies.
- Triggering request validation.
- Delegating operations to the Service layer.
- Returning appropriate HTTP responses and status codes.

Input validation is triggered using Jakarta Bean Validation through annotations such as:

- `@Valid`
- `@NotBlank`
- `@Size`

Controllers do not contain business rules.

---

### Service

The Service layer contains the application's business logic and coordinates use-case execution.

Responsibilities include:

- Applying business rules.
- Validating application state.
- Retrieving and persisting entities through repositories.
- Coordinating DTO/entity conversion through mappers.
- Throwing domain-specific application exceptions when operations cannot be completed.

Examples of service responsibilities include:

- Verifying resource existence.
- Preventing duplicate categories or products when required.
- Activating and deactivating resources.
- Authenticating users.
- Generating JWTs through the authentication flow.

Keeping these responsibilities outside controllers prevents HTTP concerns from becoming coupled to business logic.

---

### Repository

The Repository layer is responsible for persistence operations using Spring Data JPA.

Repositories abstract database access and communicate with PostgreSQL through JPA/Hibernate.

Standard CRUD operations are provided by Spring Data repositories, while additional behavior can be expressed through derived query methods.

Examples include:

```java
existsByName(String name)
```

and queries that support uniqueness validation during update operations.

Application-level uniqueness checks complement constraints defined directly in the database.

---

### Domain

The Domain layer represents the application's persistent business entities.

Current domain concepts include:

- Product
- Category
- User
- Role

Entities define relationships and persistence mappings used by JPA/Hibernate.

The current domain package contains:

```text
domain/
└── entity/
```

Additional domain abstractions can be introduced when required by future business rules.

---

### Data Transfer Objects (DTOs)

DTOs define the external contracts of the REST API and prevent persistence entities from being exposed directly.

The application separates DTOs into:

```text
dto/
├── request/
└── response/
```

**Request DTOs** represent data received from clients.

**Response DTOs** represent data returned by the API.

This separation allows the API contract and persistence model to evolve independently.

---

### Mapper

Mappers convert data between API contracts and persistence entities.

Typical conversions include:

```text
Request DTO
     |
     v
Entity
```

and:

```text
Entity
     |
     v
Response DTO
```

Responsibilities include:

- Converting request DTOs into entities.
- Converting entities into response DTOs.
- Centralizing transformation logic.
- Preventing duplicated mapping logic across services.

This keeps the Service layer focused primarily on application behavior and business rules.

---

## Security Architecture

Security is implemented using Spring Security with stateless JWT authentication.

The main authentication flow is:

```text
Client
   |
   | email + password
   v
POST /auth/login
   |
   v
AuthService
   |
   v
Password Validation
   |
   v
JwtService
   |
   v
JWT
```

Protected requests follow:

```text
HTTP Request
     |
     | Authorization: Bearer <JWT>
     v
JwtAuthenticationFilter
     |
     v
JWT Validation
     |
     v
Spring Security Context
     |
     v
Authorization Rules
     |
     v
Controller
```

The application does not maintain authenticated HTTP sessions.

Authorization is based on:

- `ROLE_USER`
- `ROLE_ADMIN`

`ROLE_USER` can access read operations, while write operations are restricted to `ROLE_ADMIN`.

Passwords are stored using BCrypt hashing.

---

## Exception Handling

The application uses centralized exception handling to provide consistent HTTP error responses.

The `GlobalExceptionHandler` intercepts exceptions raised during request processing and converts them into structured responses.

Handled scenarios include:

- Invalid request data.
- Resource not found.
- Duplicate resources.
- Authentication failures.
- Invalid application state.

Validation errors can include information such as:

- HTTP status.
- Error description.
- General message.
- Request path.
- Invalid fields and validation messages.

This prevents exception-handling logic from being duplicated across controllers.

---

## Validation

Request validation is performed before validated request data reaches business operations.

Jakarta Bean Validation annotations are applied to request DTOs and activated through `@Valid` in controller methods.

This creates two validation levels:

```text
HTTP Request
     |
     v
DTO Validation
     |
     v
Service
     |
     v
Business Rule Validation
```

DTO validation protects the API contract, while Service validation protects business rules and application state.

---

## Database Architecture

PostgreSQL is used as the relational database.

Database schema creation and evolution are managed through **Flyway SQL migrations**.

```text
Migration Scripts
       |
       v
     Flyway
       |
       v
 PostgreSQL Schema
       ^
       |
Hibernate Validation
```

Flyway acts as the source of truth for schema evolution.

Hibernate is configured with:

```yaml
ddl-auto: validate
```

Therefore, Hibernate validates entity mappings against the existing schema instead of creating or modifying database structures automatically.

This keeps database changes explicit, versioned, and reproducible.

---

## Main Data Relationships

### Product and Category

```text
Category
   1
   |
   |
   N
Product
```

A category can contain multiple products, while each product belongs to a category.

### User and Role

```text
User
  N
  |
  |
  N
Role
```

The many-to-many relationship is represented by:

```text
user_roles
```

---

## Project Structure

```text
src/main/java/io/github/kathleenfs/productmanagementapi/
│
├── config/
├── controller/
├── domain/
│   └── entity/
├── dto/
│   ├── request/
│   └── response/
├── exception/
├── mapper/
├── repository/
├── security/
└── service/
```

Supporting resources:

```text
src/main/resources/
│
├── db/
│   └── migration/
│
└── application.yml
```

Automated tests are maintained separately under:

```text
src/test/java/
```

---

## Architectural Principles

The project applies the following principles and practices:

- Layered Architecture
- Separation of Concerns
- Dependency Injection
- DTO Pattern
- Repository Pattern
- Centralized Object Mapping
- RESTful API Design
- SOLID Principles
- Clean Code Practices
- Stateless Authentication
- Role-Based Access Control
- Centralized Exception Handling
- Database Versioning
- Externalized Security Configuration
- Automated Testing

---

## Testing Strategy

The architecture supports isolated testing by keeping business logic separated from infrastructure concerns.

Service dependencies are mocked using Mockito:

```text
Service
   |
   +---- Mock Repository
   |
   +---- Mock Mapper
```

This allows business rules to be tested without requiring PostgreSQL or an HTTP server.

Selected HTTP behavior is tested using MockMvc.

JaCoCo is used to analyze code coverage and identify execution paths that are not exercised by the automated test suite.

---

## Architecture Status

| Area | Status |
|---|---|
| Layered Architecture | ✅ Completed |
| Domain Model | ✅ Completed |
| DTOs and Mapping | ✅ Completed |
| Validation | ✅ Completed |
| Exception Handling | ✅ Completed |
| Persistence | ✅ Completed |
| Flyway Migrations | ✅ Completed |
| JWT Authentication | ✅ Completed |
| Role-Based Authorization | ✅ Completed |
| Automated Testing | ✅ Completed |
| JaCoCo Coverage | ✅ Completed |
| Swagger / OpenAPI | ✅ Completed |
| PostgreSQL Docker Environment | ✅ Completed |

---

## Architecture Scope

Version `1.0.0` is implemented as a **single Spring Boot application using Layered Architecture**.

The current architecture is not based on microservices or Hexagonal Architecture. Those architectural styles require different boundaries and dependency organization and are intentionally outside the scope of this application.

The purpose of the current structure is to maintain explicit separation between API, business, persistence, mapping, security, and infrastructure responsibilities while keeping the application cohesive.