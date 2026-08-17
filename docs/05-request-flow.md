# Request Flow

## Overview

The Product Management API follows a layered request flow.

Each HTTP request passes through the appropriate security, validation, business, persistence, and mapping responsibilities before a response is returned to the client.

---

## Standard Request Flow

A typical authenticated request follows this sequence:

```text
Client
  |
  | HTTP Request
  v
Spring Security
  |
  v
JwtAuthenticationFilter
  |
  v
Authentication / Authorization
  |
  v
Controller
  |
  v
Request Validation
  |
  v
Service
  |
  +--------> Mapper
  |
  v
Repository
  |
  v
PostgreSQL
  |
  v
Repository
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

## Authentication Flow

Authentication is performed through:

```http
POST /auth/login
```

The flow is:

```text
Client
  |
  | email + password
  v
AuthController
  |
  v
AuthService
  |
  v
UserRepository
  |
  v
User
  |
  v
PasswordEncoder.matches()
  |
  +---- Invalid credentials
  |         |
  |         v
  |   Authentication Error
  |
  v
User Active Validation
  |
  +---- Inactive user
  |         |
  |         v
  |   Application Error
  |
  v
JwtService
  |
  v
JWT Generated
  |
  v
LoginResponseDTO
  |
  v
HTTP Response
```

The generated token is returned to the client and used in subsequent protected requests.

---

## Protected Request Flow

Protected endpoints require the JWT in the request header:

```text
Authorization: Bearer <JWT>
```

The flow is:

```text
HTTP Request
     |
     v
JwtAuthenticationFilter
     |
     v
Extract Bearer Token
     |
     v
JwtService
     |
     v
Extract User Email
     |
     v
CustomUserDetailsService
     |
     v
Load User
     |
     v
Validate Token
     |
     v
Create Authentication
     |
     v
SecurityContext
     |
     v
Authorization Rules
     |
     v
Controller
```

If the token is invalid or the user does not have the required role, the request does not continue to the protected operation.

---

## Authorization Flow

Authorization rules are evaluated by Spring Security before protected operations are executed.

The application uses:

```text
ROLE_USER
ROLE_ADMIN
```

Example:

```text
GET /products
     |
     +---- ROLE_USER  -> Allowed
     |
     +---- ROLE_ADMIN -> Allowed
```

```text
POST /products
     |
     +---- ROLE_USER  -> 403 Forbidden
     |
     +---- ROLE_ADMIN -> Allowed
```

---

## Product Creation Flow

Example endpoint:

```http
POST /products
```

The request body contains product information and a category identifier.

```text
Client
  |
  | ProductRequestDTO
  v
ProductController
  |
  v
Bean Validation
  |
  +---- Invalid DTO
  |         |
  |         v
  |   GlobalExceptionHandler
  |         |
  |         v
  |   400 Bad Request
  |
  v
ProductService
  |
  v
CategoryRepository.findById(categoryId)
  |
  +---- Category not found
  |         |
  |         v
  |   ResourceNotFoundException
  |
  v
ProductMapper.toEntity()
  |
  v
Associate Category
  |
  v
ProductRepository.save()
  |
  v
ProductMapper.toResponse()
  |
  v
ProductResponseDTO
  |
  v
201 Created
```

---

## Product Update Flow

Example endpoint:

```http
PUT /products/{id}
```

```text
Client
  |
  v
ProductController
  |
  v
ProductService
  |
  v
ProductRepository.findById(id)
  |
  +---- Product not found
  |         |
  |         v
  |   ResourceNotFoundException
  |
  v
CategoryRepository.findById(categoryId)
  |
  +---- Category not found
  |         |
  |         v
  |   ResourceNotFoundException
  |
  v
Update Entity Fields
  |
  v
ProductRepository.save()
  |
  v
Mapper
  |
  v
ProductResponseDTO
```

---

## Product Deactivation Flow

The application uses logical deactivation instead of removing the record from the database.

Example:

```http
DELETE /products/{id}
```

Flow:

```text
ProductController
      |
      v
ProductService
      |
      v
ProductRepository.findById(id)
      |
      v
product.active = false
      |
      v
ProductRepository.save()
      |
      v
204 No Content
```

The database record remains persisted.

---

## Product Activation Flow

Example:

```http
PATCH /products/{id}/activate
```

```text
ProductController
      |
      v
ProductService
      |
      v
ProductRepository.findById(id)
      |
      v
product.active = true
      |
      v
ProductRepository.save()
      |
      v
ProductResponseDTO
```

---

## Category Flow

Category operations follow the same layered structure used for products:

```text
CategoryController
       |
       v
CategoryService
       |
       v
CategoryRepository
       |
       v
PostgreSQL
```

Category business rules include:

- Resource existence validation
- Category name uniqueness
- Activation
- Deactivation

During updates, category uniqueness is validated while excluding the current category ID.

---

## Validation Flow

Input validation happens before business logic execution.

Example:

```text
HTTP Request
     |
     v
Request DTO
     |
     v
Bean Validation
     |
     +---- Valid ------> Controller / Service
     |
     +---- Invalid ----> GlobalExceptionHandler
                              |
                              v
                        400 Bad Request
```

Validation annotations include:

```text
@NotBlank
@NotNull
@Size
@Min
@DecimalMin
```

---

## Exception Flow

Application exceptions are centralized through `GlobalExceptionHandler`.

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
ApiErrorResponse
        |
        v
HTTP Response
```

This prevents exception handling from being duplicated across controllers.

Examples include:

- `ResourceNotFoundException`
- `CategoryAlreadyExistsException`
- Validation exceptions
- Authentication-related exceptions

---

## Database Flow

Persistence operations are abstracted through Spring Data JPA repositories.

```text
Service
   |
   v
Repository
   |
   v
JPA / Hibernate
   |
   v
PostgreSQL
```

The Service layer does not communicate directly with SQL or database connections.

Flyway remains responsible for database schema versioning, while Hibernate manages runtime persistence and entity mapping.

---

## Response Flow

Entities are not exposed directly through the REST API.

Responses follow:

```text
Entity
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
JSON Response
```

This keeps the persistence model separated from the public API contract.

---

## Flow Responsibilities

| Component | Responsibility |
|---|---|
| Spring Security | Authentication and authorization |
| JwtAuthenticationFilter | JWT extraction and request authentication |
| Controller | HTTP request and response handling |
| DTO | API input and output contracts |
| Bean Validation | Request validation |
| Service | Business rules and orchestration |
| Mapper | DTO/entity transformation |
| Repository | Persistence abstraction |
| Hibernate | ORM and persistence operations |
| PostgreSQL | Relational data storage |
| GlobalExceptionHandler | Standardized error responses |

---

## Summary

The request flow is designed to keep responsibilities separated:

```text
Security
   |
Controller
   |
Validation
   |
Service
   |
Repository
   |
Database
```

DTOs, mappers, centralized exception handling, and JWT security complement this flow without coupling persistence concerns directly to the HTTP layer.