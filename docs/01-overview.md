# Product Management API

## Overview

Product Management API is a RESTful backend application developed with Java 21 and Spring Boot for managing products and categories.

The application follows a layered architecture with clear separation between HTTP handling, business logic, data transformation, persistence, and security.

It provides stateless authentication with JWT, role-based authorization, relational persistence with PostgreSQL, database versioning with Flyway, request validation, centralized exception handling, automated testing, code coverage analysis, and interactive API documentation.

---

## Objectives

The main objectives of the application are:

- Provide RESTful endpoints for product and category management.
- Apply a layered architecture with separation of concerns.
- Separate API contracts from persistence entities using DTOs.
- Persist relational data using PostgreSQL and Spring Data JPA.
- Manage database schema evolution with Flyway.
- Implement stateless authentication using JWT.
- Apply role-based authorization with `ROLE_USER` and `ROLE_ADMIN`.
- Protect user passwords using BCrypt.
- Validate incoming requests using Bean Validation.
- Centralize application exception handling.
- Provide interactive API documentation with OpenAPI and Swagger UI.
- Validate business rules through automated tests.
- Analyze test coverage using JaCoCo.
- Provide a reproducible PostgreSQL development environment with Docker Compose.

---

## Main Features

### Product Management

- Product creation
- Product listing
- Product retrieval by ID
- Product update
- Product activation
- Product deactivation
- Product and category association
- Stock quantity persistence

### Category Management

- Category creation
- Category listing
- Category retrieval by ID
- Category update
- Category activation
- Category deactivation

### Authentication and Authorization

- User registration
- User authentication
- JWT generation and validation
- Stateless authentication
- Role-based access control
- `ROLE_USER` access to read operations
- `ROLE_ADMIN` access to read and write operations
- BCrypt password hashing

### API and Data Management

- RESTful API design
- Request and response DTOs
- Request validation
- Global exception handling
- PostgreSQL persistence
- Flyway database migrations
- OpenAPI specification
- Swagger UI with JWT Bearer authentication

### Quality and Testing

- Unit testing with JUnit 5
- Dependency mocking with Mockito
- Web-layer testing with MockMvc
- Code coverage analysis with JaCoCo

### Development Infrastructure

- PostgreSQL with Docker Compose
- Maven-based build
- Environment-based JWT configuration

---

## Technology Stack

### Backend

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Maven
- Lombok

### Security

- Spring Security
- JWT
- BCrypt
- Role-Based Access Control

### Database

- PostgreSQL
- Flyway

### Testing

- JUnit 5
- Mockito
- MockMvc
- JaCoCo

### Documentation

- OpenAPI
- Swagger UI

### Infrastructure

- Docker
- Docker Compose

---

## Architectural Style

The application is implemented as a single Spring Boot application using a layered architecture.

```text
Controller
     |
     v
Service
     |
     +------> Mapper
     |
     v
Repository
     |
     v
PostgreSQL
```

The main responsibilities are separated into:

- **Controller:** HTTP endpoints and request handling.
- **Service:** business rules and application logic.
- **Repository:** persistence abstraction.
- **Mapper:** conversion between entities and DTOs.
- **DTO:** external API contracts.
- **Security:** authentication and authorization.
- **Exception:** centralized error handling.

This project is not structured as a microservices architecture. Its scope is intentionally contained within a single application and focuses on layered backend design.

---

## Security Model

Authentication is stateless and based on JWT.

After successful login, clients receive a token that is sent with protected requests:

```text
Authorization: Bearer <JWT>
```

Authorization is based on two roles:

| Role | Read Operations | Write Operations |
|---|---|---|
| `ROLE_USER` | Allowed | Forbidden |
| `ROLE_ADMIN` | Allowed | Allowed |

Swagger UI is configured to support JWT Bearer authentication, allowing protected endpoints to be executed directly from the API documentation.

---

## Persistence

PostgreSQL is used as the relational database.

The main domain relationship is:

```text
Category 1 -------- N Product
```

User authorization uses a many-to-many relationship between users and roles:

```text
Users N -------- N Roles
         user_roles
```

Database schema evolution is managed through versioned Flyway migrations.

Hibernate is configured with:

```text
ddl-auto: validate
```

This allows Hibernate to validate the entity mapping against the schema while Flyway remains responsible for schema creation and evolution.

---

## Testing Strategy

Business rules are primarily tested at the service layer using JUnit 5 and Mockito.

The test suite covers successful operations as well as relevant failure scenarios, including:

- Resource creation and retrieval
- Updates
- Activation and deactivation
- Duplicate resources
- Resources not found
- Authentication failures
- Inactive users
- JWT generation and validation

Selected HTTP behavior is tested with MockMvc, including request validation and response status codes.

JaCoCo is used to analyze code coverage and identify untested execution paths.

---

## API Documentation

Interactive documentation is available through Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

---

## Project Status

**Completed — Version 1.0.0**

The initial scope of the Product Management API is complete.

Core functionality includes product and category management, authentication, authorization, persistence, database migrations, validation, exception handling, automated testing, code coverage, API documentation, and a containerized PostgreSQL development environment.

Future capabilities such as pagination, filtering, caching, observability, CI/CD, and cloud deployment are intentionally outside the current version and documented separately as possible extensions.