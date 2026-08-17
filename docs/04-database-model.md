# Data Model

## Overview

The Product Management API uses PostgreSQL as its relational database.

The database schema is managed through Flyway migrations, while Hibernate validates the mapping between the database schema and JPA entities.

The main domain relationships are:

```text
Category 1 -------- N Product

User N -------- N Role
        user_roles
```

---

## Entities

### Category

The `category` table represents a logical group of products.

A category can contain multiple products.

| Field | Type | Description |
|---|---|---|
| `id` | BIGINT | Primary key |
| `name` | VARCHAR(100) | Unique category name |
| `description` | VARCHAR(500) | Optional category description |
| `active` | BOOLEAN | Indicates whether the category is active |
| `created_at` | TIMESTAMP | Creation timestamp |
| `updated_at` | TIMESTAMP | Last update timestamp |

### Category Constraints

- Primary key: `pk_category`
- Unique constraint: `uk_category_name`
- `name` is required
- `active` is required
- `created_at` is required
- `updated_at` is required

---

### Product

The `product` table represents a product managed by the application.

Each product belongs to a category.

Main product information includes:

| Field | Description |
|---|---|
| `id` | Product primary key |
| `name` | Product name |
| `description` | Product description |
| `price` | Product price |
| `stock_quantity` | Available stock quantity |
| `active` | Indicates whether the product is active |
| `category_id` | Foreign key referencing the product category |
| `created_at` | Creation timestamp |
| `updated_at` | Last update timestamp |

### Product Relationship

```text
Category
    |
    | 1
    |
    | N
    v
Product
```

The `category_id` foreign key associates each product with a category.

---

### User

The `users` table stores application users used for authentication and authorization.

User information includes:

| Field | Description |
|---|---|
| `id` | User primary key |
| `name` | User name |
| `email` | User authentication email |
| `password` | BCrypt password hash |
| `active` | Indicates whether the user can authenticate |

Passwords are never stored in plain text.

---

### Role

The `roles` table stores the authorization roles available in the application.

Current roles:

```text
ROLE_USER
ROLE_ADMIN
```

Roles determine which operations an authenticated user can perform.

---

### User Roles

The `user_roles` table represents the many-to-many relationship between users and roles.

```text
Users
  |
  | N
  |
  v
user_roles
  ^
  | N
  |
Roles
```

This allows a user to have multiple roles without storing authorization information directly in the `users` table.

---

## Entity Relationships

### Category and Product

```text
+------------------+
|     category     |
+------------------+
| id PK            |
| name             |
| description      |
| active           |
| created_at       |
| updated_at       |
+--------+---------+
         |
         | 1
         |
         | N
         v
+------------------+
|      product     |
+------------------+
| id PK            |
| name             |
| description      |
| price            |
| stock_quantity   |
| active           |
| category_id FK   |
| created_at       |
| updated_at       |
+------------------+
```

### User and Role

```text
+---------------+
|     users     |
+---------------+
| id PK         |
| name          |
| email         |
| password      |
| active        |
+-------+-------+
        |
        | N
        v
+----------------+
|   user_roles   |
+----------------+
| user_id FK     |
| role_id FK     |
+-------+--------+
        |
        | N
        v
+---------------+
|     roles     |
+---------------+
| id PK         |
| name          |
+---------------+
```

---

## Database Versioning

Database schema changes are managed using Flyway.

Migration files are stored in:

```text
src/main/resources/db/migration
```

Flyway applies pending migrations when the application starts.

Hibernate is configured with:

```yaml
ddl-auto: validate
```

Therefore:

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

Flyway remains responsible for database schema evolution, while Hibernate validates that the entity mappings match the existing schema.

---

## Data Integrity

Data integrity is enforced at both application and database levels.

### Application Level

The Service layer validates business rules such as:

- Resource existence
- Duplicate resources
- Valid relationships between products and categories
- Activation and deactivation state

### Database Level

The relational database provides structural guarantees through:

- Primary keys
- Foreign keys
- Unique constraints
- Required columns
- Relational integrity

This combination prevents the application from relying exclusively on either application-level or database-level validation.