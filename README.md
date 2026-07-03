# Product Management API

Enterprise-grade REST API for product management built with Java and Spring Boot, following industry best practices for clean architecture, maintainability, security, and scalability.

This project is part of a backend portfolio that demonstrates the development of production-ready applications using modern Java technologies and software engineering principles.

---

## Features

* Product CRUD operations
* RESTful API design
* Layered Architecture
* Request validation
* Global exception handling
* Authentication and authorization with JWT
* Relational database integration
* Database versioning with Flyway
* Interactive API documentation with Swagger/OpenAPI
* Unit and integration testing
* Dockerized application

---

## Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Flyway
* Spring Validation
* Spring Security
* JWT
* Swagger / OpenAPI
* JUnit
* Mockito
* Docker

---

## Architecture

The application follows a layered architecture to ensure separation of concerns, maintainability, and scalability.

```text
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Database
```

Main architectural principles:

* Separation of responsibilities
* Dependency Injection
* SOLID principles
* Clean code practices
* Exception handling
* Validation
* Stateless authentication

---

## Request Flow

```text
HTTP Request
      │
      ▼
Controller
      │
      ▼
Validation
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
PostgreSQL
      │
      ▼
HTTP Response
```

---

## Database Model

> Entity Relationship Diagram (ERD)

*(Diagram will be added.)*

---

## API Documentation

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI specification is available after running the application.

---

## Testing

Tests include:

* Unit Tests
* Service Tests
* Repository Tests
* Integration Tests

Frameworks:

* JUnit
* Mockito

---

## Running the Project

### Requirements

* Java 21
* Maven
* Docker
* PostgreSQL

### Clone

```bash
git clone https://github.com/your-user/product-management-api.git
```

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

Or using Docker:

```bash
docker compose up --build
```

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── ...
│   └── resources
│       ├── db
│       ├── application.yml
│       └── ...
└── test
```

---

## Future Improvements

* Pagination and sorting
* Advanced filtering
* API versioning
* Caching with Redis
* Metrics and monitoring
* CI/CD pipeline
* Cloud deployment

---

## License

This project is intended for educational and portfolio purposes.
