# System Requirements

## Functional Requirements

### Product Management

The system must allow authenticated users to retrieve product information and administrators to manage product data.

- Create products.
- Update product information.
- Retrieve products by ID.
- List all products.
- Activate products.
- Deactivate products.
- Associate products with categories.
- Store and update product stock quantities.

### Category Management

The system must provide category management capabilities.

- Create categories.
- Update category information.
- Retrieve categories by ID.
- List all categories.
- Activate categories.
- Deactivate categories.
- Associate categories with products.

### User Management

The system must provide user registration and authentication capabilities.

- Register users.
- Store passwords securely using BCrypt.
- Authenticate users using email and password.
- Prevent inactive users from authenticating.
- Associate users with application roles.

### Authentication and Authorization

The system must control access to protected resources using JWT authentication and role-based authorization.

- Generate a JWT after successful authentication.
- Validate JWTs on protected requests.
- Maintain stateless authentication.
- Allow authenticated `ROLE_USER` and `ROLE_ADMIN` users to access read operations.
- Restrict write operations to `ROLE_ADMIN`.
- Return appropriate HTTP status codes for unauthorized and forbidden operations.

---

## Non-Functional Requirements

### Architecture

The application must follow a layered architecture with clear separation of responsibilities.

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
PostgreSQL
```

Architectural requirements include:

- Controllers must handle HTTP requests and responses.
- Business rules must remain in the Service layer.
- Repositories must abstract database access.
- DTOs must define external API contracts.
- Mappers must handle conversions between entities and DTOs.
- Security responsibilities must remain separated from business logic.
- Dependencies must be managed through Dependency Injection.

### Security

The application must provide stateless authentication and role-based authorization.

- Protected endpoints must require JWT authentication.
- Passwords must be stored using BCrypt hashing.
- JWT signing secrets must be provided through environment configuration.
- Authentication must not rely on server-side HTTP sessions.
- Authorization must distinguish between `ROLE_USER` and `ROLE_ADMIN`.
- Administrative operations must require `ROLE_ADMIN`.

### Validation and Error Handling

The application must validate incoming data and provide consistent error responses.

- Request DTOs must be validated before business logic execution.
- Invalid requests must return appropriate HTTP status codes.
- Resources that do not exist must produce controlled error responses.
- Duplicate resources must be handled explicitly.
- Application exceptions must be processed through centralized exception handling.

### Database

PostgreSQL must be used as the relational database.

- Persistence must be implemented using Spring Data JPA and Hibernate.
- Database schema changes must be versioned using Flyway.
- Flyway must be responsible for schema evolution.
- Hibernate must validate the database schema rather than create it automatically.
- Products must maintain a relationship with categories.
- Users and roles must maintain a many-to-many relationship through `user_roles`.

### API Design

The application must expose resources through RESTful HTTP endpoints.

- HTTP methods must represent the intended resource operations.
- Request and response models must use DTOs.
- Appropriate HTTP status codes must be returned.
- API resources must use consistent endpoint naming.

### Documentation

The API must provide interactive documentation using OpenAPI and Swagger UI.

- Available endpoints must be exposed through OpenAPI.
- Request and response schemas must be available through Swagger UI.
- Swagger UI must support JWT Bearer authentication.
- Technical project documentation must be maintained in the `docs` directory.

### Testing

Business rules and selected HTTP behavior must be covered by automated tests.

- Service-layer business logic must be tested using JUnit 5.
- Dependencies must be isolated using Mockito where appropriate.
- Successful and failure scenarios must be tested.
- Selected controller behavior must be tested using MockMvc.
- Request validation behavior must be tested.
- JWT generation and validation behavior must be tested.
- Test coverage must be analyzed using JaCoCo.

### Configuration

Application configuration must support externalized security settings.

Required configuration:

```text
JWT_SECRET
```

Optional configuration:

```text
JWT_EXPIRATION
```

If `JWT_EXPIRATION` is not provided, the application must use the configured default value.

Secrets must not be committed to source control.

### Development Environment

The project must provide a reproducible local database environment.

- PostgreSQL must be executable through Docker Compose.
- The application must connect to the containerized PostgreSQL instance.
- Database migrations must execute during application startup.
- The application must be buildable using Maven.

---

## Access Control Requirements

| Operation | ROLE_USER | ROLE_ADMIN |
|---|:---:|:---:|
| Register | Public | Public |
| Login | Public | Public |
| List products | Allowed | Allowed |
| Find product by ID | Allowed | Allowed |
| Create product | Forbidden | Allowed |
| Update product | Forbidden | Allowed |
| Activate product | Forbidden | Allowed |
| Deactivate product | Forbidden | Allowed |
| List categories | Allowed | Allowed |
| Find category by ID | Allowed | Allowed |
| Create category | Forbidden | Allowed |
| Update category | Forbidden | Allowed |
| Activate category | Forbidden | Allowed |
| Deactivate category | Forbidden | Allowed |

---

## Version 1.0 Scope

Version `1.0.0` includes:

- Product management
- Category management
- User registration
- JWT authentication
- Role-based authorization
- PostgreSQL persistence
- Flyway migrations
- Request validation
- Centralized exception handling
- Swagger/OpenAPI documentation
- Automated testing
- JaCoCo coverage analysis
- Docker Compose PostgreSQL environment

The following capabilities are intentionally outside the current version and may be introduced in future versions:

- Product search
- Dynamic filtering
- Pagination and sorting
- Advanced inventory management
- Redis caching
- Observability and metrics
- CI/CD
- Cloud deployment