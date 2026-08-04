# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 3.2.1 / Java 17 REST backend (Maven), with JWT-based authentication and MySQL persistence. Base package: `com.example.springbootbackend`.

## Build & run

```
./mvnw clean install        # build
./mvnw spring-boot:run       # run locally (requires a reachable MySQL instance)
./mvnw test                  # run all tests
./mvnw test -Dtest=ClassName#methodName   # run a single test
```

Do not run build/test/lint commands yourself unless the user explicitly asks — the user runs and verifies the application manually.

Docker: `docker compose up --build` (see `compose.yaml`) starts the app (port 8081→8080) and a MySQL 8.0 container (port 3308→3306). Env vars `DATABASE_HOST`, `DATABASE_USER`, `DATABASE_PASSWORD`, `DATABASE_NAME` override `src/main/resources/application.yml` defaults (`localhost:3306/my-project-db`). `spring.jpa.hibernate.ddl-auto` is `update`, so schema changes to `@Entity` classes apply automatically on startup — there are no Flyway/Liquibase migrations in this repo.

## Architecture

The app is organized by **feature package** (`auth`, `user`, `category`, `vocabulary`, `config`, `exceptions`, `demo`), and each feature (except `auth`) follows the same layered sub-structure:

```
<feature>/controller   REST endpoints
<feature>/service      business logic, injected repositories
<feature>/repository   Spring Data JPA interfaces
<feature>/model        @Entity classes
<feature>/DTO or dto   request/response DTOs (casing is inconsistent between features)
<feature>/mapper       stateless entity<->DTO mapping, exposed as static methods (e.g. `CategoryMapper.toDTO(...)`, not injected beans)
```

`auth` is flat (no sub-packages): `AuthenticationController`, `AuthenticationService`, `AuthenticationRequest`, `AuthenticationResponse`, `RegisterRequest`.

### Domain relationships

- `Vocabulary` (e.g. "Legal Entity", "Business Unit") has many `Category` entities.
- `Category` has an optional self-referential `parentCategory`, allowing category trees within a vocabulary.
- `User` has a `Role` (`USER`, `ADMIN`), and each `Role` maps to a fixed `Set<Permission>` (`user/enums/Permission.java`, `user/enums/Role.java`). `Role.getAuthorities()` produces both the granular permission authorities and a `ROLE_*` authority.

### Auth & security (`config/`, `auth/`)

- Stateless JWT auth: `JwtAuthenticationFilter` runs before `UsernamePasswordAuthenticationFilter`; `JwtService` issues/validates tokens; `ApplicationConfig` wires `AuthenticationProvider`/`PasswordEncoder`/`AuthenticationManager`.
- `SecurityConfiguration` disables CSRF, sets `SessionCreationPolicy.STATELESS`, and permits all requests under `/api/v1/auth/**` and `api/v1/demo-controller/**`; everything else requires authentication. Method-level security is enabled (`@EnableMethodSecurity`).
- `AuthenticationService.register`/`authenticate` return an `AuthenticationResponse` (id + JWT). Login failures use the custom exception below rather than Spring Security's default exceptions.

### Error handling (`exceptions/`)

- Domain errors should be raised as `MyProjectException(MyProjectError, optionalDescription)` — `MyProjectError` is an enum defining an `HttpStatus` and default description per error code; the optional description overrides the default at throw time.
- `GlobalExceptionHandler` (`@RestControllerAdvice`) maps `MyProjectException`, bean-validation failures (`MethodArgumentNotValidException`, `ConstraintViolationException`), and `ResponseStatusException` to HTTP responses. New domain exceptions should go through `MyProjectError`/`MyProjectException` rather than ad hoc `RuntimeException`s, though `category`'s service layer currently predates this convention and still throws raw `RuntimeException`s for not-found cases.
