---

# Event-Driven Job Application Tracker (Backend)

This project is a backend system for tracking job applications and interview workflows.
It started as a simple CRUD project but evolved into an event-driven architecture using Kafka, JWT-based authentication, and a JSONB activity log for audit tracking.

The goal of this project was to build something closer to a real backend system instead of a basic CRUD demo.

---

## Overview

A user can:

* Register and login (JWT authentication)
* Create job applications
* Update application status (APPLIED → INTERVIEWING → OFFER / REJECTED)
* Schedule interviews for applications
* View only their own data (strict ownership enforcement)

Behind the scenes:

* Domain events are published to Kafka
* Consumers process those events asynchronously
* Events are stored in a structured `activity_log` table using PostgreSQL JSONB

---

## Tech Stack

* Java 17
* Spring Boot 3.2.x
* Spring Security (Stateless JWT)
* PostgreSQL
* Flyway (Database migrations)
* Apache Kafka
* Docker Compose
* JPA / Hibernate
* Swagger (OpenAPI)
* Integration Testing (SpringBootTest)

---

## Authentication Design

* Stateless JWT authentication
* Custom `JwtAuthFilter`
* No HTTP sessions
* All endpoints protected except:

    * `/api/auth/**`
    * Swagger endpoints

Ownership enforcement is done at the service layer by resolving the current user from `SecurityContextHolder` and filtering queries by `userId`.

---

## Event-Driven Flow

When something important happens (like creating an application or scheduling an interview):

1. Data is saved to PostgreSQL
2. A domain event is published to Kafka
3. A Kafka consumer receives the event
4. The consumer persists the event into an `activity_log` table

This decouples write operations from audit/event processing.

---

## Activity Log (JSONB-Based Audit Trail)

The system maintains an `activity_log` table for tracking domain events.

```sql
CREATE TABLE activity_log (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT,
    application_id BIGINT,
    interview_id BIGINT,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
```

### Why JSONB?

* Flexible structure for evolving event payloads
* No rigid schema coupling
* Efficient indexing support in PostgreSQL
* Suitable for audit/event logging

Indexes added:

* `(application_id, created_at DESC)`
* `(interview_id, created_at DESC)`
* `(created_at DESC)`

---

## API Endpoints

### Auth

* `POST /api/auth/register`
* `POST /api/auth/login`

### Applications

* `POST /api/applications`
* `GET /api/applications`
* `PATCH /api/applications/{id}/status`

### Interviews

* `POST /api/applications/{applicationId}/interviews`
* `GET /api/applications/{applicationId}/interviews`

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

---

## System Flow

```
Client (Postman / Swagger)
        |
        v
Controller
        |
        v
Service Layer
        |
        v
PostgreSQL (applications / interviews)
        |
        v
Kafka Producer  --->  Kafka Topic  --->  Kafka Consumer
                                          |
                                          v
                                   activity_log (JSONB)
```

---

## Running the Project

Start infrastructure:

```
docker-compose up -d
```

Run the application:

```
mvn spring-boot:run
```

---

## Verifying Event Persistence

After scheduling an interview:

```
docker exec -it jobtracker-postgres psql -U jobtracker -d jobtracker \
-c "SELECT event_type, entity_type, application_id, interview_id FROM activity_log;"
```

You should see `InterviewScheduledEvent` entries.

---

## Design Focus

* Proper separation of concerns (controller → service → repository)
* Stateless security
* Ownership enforcement
* Event-driven decoupling
* Database version control with Flyway
* Integration testing for auth and access control

This project is backend-focused and does not include a frontend.

---

## Future Improvements

* Deploy to cloud (Render / AWS)
* Add metrics and observability
* Add activity log API endpoint
* Improve test coverage

---

