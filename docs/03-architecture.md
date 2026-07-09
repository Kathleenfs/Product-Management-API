# Architecture

## Overview

The Product Management API follows a Layered Architecture, where each layer has a single responsibility. This approach improves maintainability, readability, testability, and scalability.

The application is organized into independent layers that communicate in a single direction.

---

## Architecture Diagram

```text
HTTP Request
      │
      ▼
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
PostgreSQL Database
      │
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

Repositories communicate directly with the database.

---

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
├── dto
├── entity
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

* Separation of Concerns
* Layered Architecture
* SOLID Principles
* RESTful API Design
* Database Versioning
* Stateless Authentication
* Clean Code Practices
