# Scalable Ride-Sharing Backend (Java Microservices)

## Overview

This project is a scalable ride-sharing backend built with Java microservices architecture. It simulates a production-style system similar to modern ride-sharing platforms, focusing on clean architecture, service isolation, and scalability.

The system is composed of multiple independent services communicating through REST APIs and routed via an centralized API Gateway. Each service is containerized and designed to support multi-tenant regional expansion.

This project demonstrates practical backend engineering skills including microservices design, authentication, database architecture, and containerized deployment.

---

## Architecture

### Services

* **User Service**

  * User registration and authentication
  * JWT-based authorization
  * Profile management

* **Trip Service**

  * Create and join trips
  * Trip matching logic
  * Multi-tenant support by region

* **Payment Service (Mock)**

  * Simulated wallet and transaction handling

* **API Gateway**

  * Central routing entry point
  * Authentication filtering
  * Service orchestration

### Architecture Principles

* Microservices architecture
* Clean architecture and separation of concerns
* Database per service
* Multi-tenant design
* Containerized deployment

---

## Tech Stack

* Java 17
* Spring Boot
* Spring Cloud Gateway
* PostgreSQL
* Docker
* JWT Authentication
* Maven
* Swagger / OpenAPI

---

## Project Structure

```
ride-sharing-backend/
├── api-gateway/
├── user-service/
├── trip-service/
├── payment-service/
├── docker-compose.yml
└── README.md
```

Each service follows a layered architecture:

```
controller → service → repository → database
```

---

## Features

* RESTful API design
* JWT authentication and authorization
* Multi-tenant database architecture
* Trip lifecycle management
* Mock payment transactions
* API Gateway routing
* Dockerized microservices
* API documentation with Swagger
* Logging and error handling

---

## Multi-Tenant Design

The system supports multiple regions using tenant isolation.

Tenant selection is handled through request headers:

```
X-Tenant-ID: <region>
```

Each tenant can map to a separate database schema or database instance, enabling regional scalability.

---

## Getting Started

### Prerequisites

* Java 17+
* Maven
* Docker & Docker Compose
* PostgreSQL

### Installation

1. Clone the repository

```
git clone https://github.com/your-username/ride-sharing-backend.git
cd ride-sharing-backend
```

2. Build services

```
mvn clean package
```

3. Start the system

```
docker-compose up --build
```

4. Access API Gateway

```
http://localhost:8080
```

---

## API Documentation

Swagger UI is available for each service:

```
http://localhost:<service-port>/swagger-ui.html
```

---

## Example API Flow

1. User authenticates via User Service
2. API Gateway validates JWT
3. Trip Service handles trip requests
4. Payment Service processes transactions

---

## Testing

Run tests for each service:

```
mvn test
```

Unit and integration tests cover authentication, trip management, and payment flows.

---

## Deployment

The system is containerized using Docker and can be deployed to any cloud platform supporting container orchestration.

Future improvements may include:

* Kubernetes deployment
* Service discovery
* Distributed caching
* Event-driven messaging

---

## Learning Goals

This project was built to demonstrate:

* Java microservices architecture
* Scalable backend system design
* Multi-tenant SaaS patterns
* Containerized deployment workflows
* Production-style engineering practices

---

## Future Enhancements

* Real payment gateway integration
* Notification service
* Event streaming with message brokers
* Monitoring and observability
* CI/CD pipeline

---

## License

This project is for educational and portfolio purposes.
