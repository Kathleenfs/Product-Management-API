# Database Model

## Overview

The application uses PostgreSQL as the relational database.

The database schema is managed through Flyway migrations following a Database First approach.

---

## Entities

### Category

The Category entity represents a logical group of products.

Each category may contain multiple products.

| Field       | Type         | Description                              |
| ----------- | ------------ | ---------------------------------------- |
| id          | BIGINT       | Primary key                              |
| name        | VARCHAR(100) | Unique category name                     |
| description | VARCHAR(500) | Optional description                     |
| active      | BOOLEAN      | Indicates whether the category is active |
| created_at  | TIMESTAMP    | Creation date                            |
| updated_at  | TIMESTAMP    | Last update date                         |
