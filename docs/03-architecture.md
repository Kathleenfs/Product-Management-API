# Architecture

## Overview

The Product Management API follows a Layered Architecture, where each layer has a single responsibility. This approach improves maintainability, readability, testability, and scalability.

The application is organized into independent layers that communicate in a single direction.

---

## Architecture Diagram

```text
HTTP Request
      |
      ▼
Controller
      |
      ▼
Request DTO
      |
      ▼
Service
      |
      ▼
Domain Model
(Entity)
      |
      ▼
Repository
      |
      ▼
PostgreSQL Database


PostgreSQL Database
      |
      ▼
Repository
      |
      ▼
Domain Model
(Entity)
      |
      ▼
Response DTO
      |
      ▼
HTTP Response
```

---

## Layers

### Controller

Responsible for receiving HTTP requests, validating input, and returning HTTP responses.

Controllers do not contain business rules.

---

### Service

Contains the application's business logic.

Services coordinate application behavior, perform validations, and communicate with repositories.

---

### Repository

Responsible for data access using Spring Data JPA.

Repositories abstract database operations and communicate directly with the PostgreSQL database, providing a clean separation between business logic and persistence logic.

Custom query methods are created using Spring Data derived query methods.

Example:

- `existsByName(String name)`

This method allows checking category uniqueness before persistence, complementing the database constraint defined by the UNIQUE key.
### Domain

Represents the core business model of the application.

It contains entities and other domain objects that describe the business concepts independently of the application layers.

Current package:

- `entity`

Future packages may include:

- `enum`
- `valueobject`

---

## Data Transfer Objects (DTOs)

DTOs are used to separate API contracts from database entities.

The application uses different DTOs for input and output:

- Request DTOs represent client input.
- Response DTOs define API responses.

This approach prevents exposing persistence details and improves maintainability.

## Mapper

The Mapper layer is responsible for converting objects between different representations.

It separates object transformation logic from business logic.

Examples:

- Request DTO → Domain Entity
- Domain Entity → Response DTO

Responsibilities:

- Convert DTOs into entities.
- Convert entities into response DTOs.
- Keep conversion logic centralized.

### Database

PostgreSQL is used as the relational database.

Database schema creation and evolution are managed through Flyway SQL migrations following a Database First approach.

Hibernate is responsible only for validating entity mappings, while Flyway is the single source of truth for database structure.

---

## Project Structure

```text
src/main/java
│
├── config
├── controller
├── domain
│   └── entity
├── dto
├── exception
├── mapper
├── repository
├── security
├── service
└── validation
```

---

## Architectural Principles

The project follows these principles:

- Separation of Concerns
- Layered Architecture
- SOLID Principles
- RESTful API Design
- Database Versioning
- Stateless Authentication
- Clean Code Practices