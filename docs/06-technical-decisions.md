# Technical Decisions

This document records the main technical decisions made during the development of the project.

---

## Java 21

Java 21 was chosen because it is the current Long-Term Support (LTS) version, offering modern language features, improved performance, and long-term maintenance.

---

## Spring Boot

Spring Boot was selected to simplify application configuration and accelerate REST API development using production-ready defaults.

---

## Layered Architecture

The project follows a layered architecture to separate responsibilities between controllers, services, repositories, and persistence.

This organization improves maintainability, readability, and testability.

---

## PostgreSQL

PostgreSQL was selected because it is a robust, open-source relational database widely adopted in enterprise environments.

---

## Docker Compose

Docker Compose is used to provide a reproducible local development environment.

Running the database inside a container eliminates installation differences between development environments.

---

## Spring Profiles

Separate configuration profiles are used to isolate development and production settings.

This approach improves configuration management across environments.

## Flyway

Flyway was selected to manage database schema versioning through SQL migration scripts.

The project follows a **Database First** approach, where database changes are defined before the Java entities are implemented. This ensures that every schema modification is tracked, versioned, and reproducible across all environments.

Using Flyway provides the following benefits:

* Database changes are version controlled.
* All environments share the same database structure.
* Schema evolution is fully traceable.
* Manual database changes are avoided.
* The complete database history is preserved.

Hibernate is configured with `ddl-auto: validate`, allowing it to validate the entity mappings without modifying the database schema. Database creation and evolution are exclusively handled by Flyway migrations.


## Data Integrity

Database constraints are used as the final layer of data protection.

Business validations are implemented in the application layer, but database constraints ensure data consistency even in unexpected scenarios such as concurrent requests or external data manipulation.