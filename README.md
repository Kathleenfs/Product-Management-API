# Product Management API

REST API for product and category management built with **Java 21** and **Spring Boot**.

The application provides JWT-based authentication, role-based authorization, PostgreSQL persistence, Flyway database migrations, automated testing, code coverage analysis, and interactive API documentation with OpenAPI.

The project follows a layered architecture with clear separation between API, business logic, persistence, security, and data transfer responsibilities.

---

## Features

- Product and category management
- User registration and authentication
- JWT-based stateless authentication
- Role-Based Access Control (RBAC) with `ROLE_USER` and `ROLE_ADMIN`
- Request validation
- Global exception handling
- PostgreSQL persistence
- Database versioning with Flyway
- Swagger / OpenAPI documentation
- Automated tests and code coverage
- PostgreSQL containerization with Docker Compose

---

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA / Hibernate
- Maven
- Lombok

### Security

- Spring Security
- JWT
- BCrypt
- Role-Based Access Control (RBAC)

### Database

- PostgreSQL
- Flyway

### Testing

- JUnit 5
- Mockito
- MockMvc
- JaCoCo

### Infrastructure & Documentation

- Docker
- Docker Compose
- OpenAPI
- Swagger UI

---

## Architecture

The application follows a layered architecture to keep HTTP handling, business logic, data transformation, and persistence separated.

```text
HTTP Request
     |
     v
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

Main practices applied:

- Separation of concerns
- Dependency Injection
- DTO pattern
- Repository pattern
- Bean Validation
- Centralized exception handling
- SOLID principles
- Clean Code practices

---

## Security

Authentication is stateless and based on **JSON Web Tokens (JWT)**.

After a successful login, the generated token must be sent with protected requests:

```text
Authorization: Bearer <JWT>
```

The API defines two roles:

| Role | Read Operations | Write Operations |
|---|---|---|
| `ROLE_USER` | Allowed | Forbidden |
| `ROLE_ADMIN` | Allowed | Allowed |

Passwords are stored using BCrypt hashing.

---

## API Overview

### Authentication

```http
POST /auth/register
POST /auth/login
```

### Products

```http
POST   /products
GET    /products
GET    /products/{id}
PUT    /products/{id}
PATCH  /products/{id}/activate
DELETE /products/{id}
```

### Categories

```http
POST   /categories
GET    /categories
GET    /categories/{id}
PUT    /categories/{id}
PATCH  /categories/{id}/activate
DELETE /categories/{id}
```

Detailed request and response schemas are available through Swagger UI.

---

## API Documentation

Interactive API documentation is available through **Swagger UI** after starting the application:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI is configured with Bearer authentication.

To access protected endpoints:

1. Register a user through `/auth/register`
2. Authenticate through `/auth/login`
3. Copy the generated JWT
4. Click **Authorize** in Swagger UI
5. Provide the JWT
6. Execute protected requests

---

## Database

PostgreSQL is used for relational persistence, while Flyway manages versioned database migrations.

Main tables:

```text
category
product
users
roles
user_roles
flyway_schema_history
```

Main relationships:

```text
Category 1 -------- N Product

Users N -------- N Roles
         user_roles
```

Migration scripts are located at:

```text
src/main/resources/db/migration
```

---

## Testing

Automated tests cover the main business rules, authentication flows, JWT behavior, exception scenarios, validation, and selected HTTP behavior.

### Tools

- JUnit 5
- Mockito
- MockMvc
- JaCoCo

Run the complete test suite:

```bash
mvn clean test
```

JaCoCo coverage report:

```text
target/site/jacoco/index.html
```

---

## Running the Project

### Requirements

- Java 21
- Maven
- Docker
- Docker Compose

### Clone the Repository

```bash
git clone https://github.com/Kathleenfs/Product-Management-API.git
cd Product-Management-API
```

### Environment Variables

The application uses environment variables for JWT configuration.

| Variable | Required | Default | Description |
|---|---|---|---|
| `JWT_SECRET` | Yes | - | Secret used to sign and validate JWT tokens |
| `JWT_EXPIRATION` | No | `3600000` | Token expiration time in milliseconds |

Configure the JWT secret before starting the application.

#### Windows PowerShell

```powershell
$env:JWT_SECRET="your-secret-key"
```

Optionally, configure the expiration time:

```powershell
$env:JWT_EXPIRATION="3600000"
```

> Secrets and production credentials should never be committed to the repository.

### Start PostgreSQL

The development database runs through Docker Compose.

| Property | Value |
|---|---|
| Database | `product_management` |
| Host | `localhost` |
| Port | `5433` |
| Username | `postgres` |

Start the database:

```bash
docker compose up -d
```

Verify the container:

```bash
docker ps
```

The application connects to:

```text
jdbc:postgresql://localhost:5433/product_management
```

Flyway automatically validates and applies pending migrations when the application starts.

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

### Stop PostgreSQL

```bash
docker compose down
```

---

## Project Structure

```text
src/
├── main/
│   ├── java/io/github/kathleenfs/productmanagementapi/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── domain/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── mapper/
│   │   ├── repository/
│   │   ├── security/
│   │   └── service/
│   │
│   └── resources/
│       ├── db/migration/
│       └── application.yml
│
└── test/
    └── java/
```

---

## Documentation

Additional technical documentation is available in the [`docs`](./docs) directory.

It contains more detailed information about:

- Requirements
- Architecture
- Data modeling
- Application flows
- Architectural decisions
- Planned improvements

---

## Future Improvements

- Pagination and sorting
- Dynamic product filtering
- Redis caching
- API versioning
- CI/CD pipeline
- Metrics and monitoring
- Centralized logging
- Cloud deployment

---

## License

This repository is intended for demonstration and technical reference purposes.