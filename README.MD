# Scalable Ride-Sharing Backend (Java Microservices)

## Overview

Lightweight Java microservices demo for a ride‑sharing backend — user, trip and payment services routed through an API Gateway. The project demonstrates multi‑tenant design, JWT auth, containerization and common backend prácticas for production‑style systems.

---

## Quick summary

- Services: `user-service`, `trip-service`, `payment-service`, `api-gateway`
- Run locally with Docker Compose
- Swagger/OpenAPI available per service (`/v3/api-docs`, `swagger-ui/index.html`)

---

## Features

- JWT authentication (tenant claim included)
- Multi-tenant support (per-request `X-Tenant-ID` or tenant claim in JWT)
- REST APIs with OpenAPI/Swagger UI
- Per-service PostgreSQL databases
- Dockerized, ready for CI builds

---

## Getting started (quick)

Prerequisites: Java 17+, Maven, Docker & Docker Compose

1. Build (optional):

```bash
mvn clean package
```

2. Start services:

```bash
docker compose up -d --build
```

3. Open Gateway / service docs:

- API Gateway: `http://localhost:8080`
- User service Swagger: `http://localhost:8081/swagger-ui/index.html`
- Trip service Swagger: `http://localhost:8082/swagger-ui/index.html`
- Payment service Swagger: `http://localhost:8083/swagger-ui/index.html`
- OpenAPI JSON (example): `http://localhost:8082/v3/api-docs`

---

## Quick API example (register → login → create trip)

1) Register a user (tenant required):

```bash
curl -sS -X POST http://localhost:8081/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"linh","email":"linh@example.com","password":"password","tenantId":"default"}'
```

2) Login → get JWT:

```bash
curl -sS -X POST http://localhost:8081/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"linh","password":"password"}'
# response: { "token": "<JWT>" }
```

3) Create a trip via API Gateway (use token OR X-Tenant-ID header):

```bash
TOKEN=<JWT_FROM_LOGIN>
curl -sS -X POST http://localhost:8080/api/trips \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -H 'X-Tenant-ID: default' \
  -d '{"passengerId":1,"pickupLocation":"Point A","destination":"Point B"}'
```

Notes:
- `X-Tenant-ID` header OR JWT claim `tenant` is required for tenant‑scoped endpoints. If both missing you will see: `{ "error": "Missing X-Tenant-ID header" }`.
- Use the **Authorize** button in Swagger UI (paste `Bearer <token>`) to call protected endpoints from the UI.

---

## Multi-tenant behavior

- Tenant is a logical string (`tenantId`) stored on resources (users, trips).
- Tenant is provided by the client via `X-Tenant-ID` header or embedded in JWT `tenant` claim.
- Services store `tenant_id` on domain entities and always scope queries by tenant.

---

## Development & tests

- Run unit + integration tests:

```bash
mvn test
```

- CI currently builds Docker images (see `.github/workflows/ci.yml`) — note: the workflow currently **skips tests** during the Maven package step. Consider enabling tests in CI.

---

## Security / secrets

- DO NOT commit secrets (e.g. `jwt.secret`, DB passwords). Move secrets to environment variables or a secret manager before production.
- Example: set `JWT_SECRET` / `SPRING_DATASOURCE_PASSWORD` via environment or Docker secrets.

---

## Troubleshooting

- Missing tenant header when calling trip endpoints → add `X-Tenant-ID` header or send JWT with `tenant` claim.
- Swagger shows `{ "error": "Missing X-Tenant-ID header" }` if you open docs on a protected path; use the service Swagger URL (docs are public) or add the header/token when calling protected APIs.

---

## What’s missing / recommended next work (high priority)

1. Automated tests: add comprehensive unit + integration tests and enable them in CI ✅
2. Externalize secrets: use env vars / Docker secrets / Vault 🔒
3. Observability: add Micrometer/Prometheus metrics + dashboards 📈
4. Kubernetes manifests / Helm charts for production deployment ⛵
5. Protect sensitive endpoints (Swagger/OpenAPI) in production, add RBAC
6. E2E tests & Postman collection

---

## Contribution

- Fork → feature branch → PR to `develop`.
- Add tests for any new behavior and update README where necessary.

---

## License

This project is for educational and portfolio purposes.

---

If you want, I can: add the Quickstart examples to README (done), enable tests in CI, or add Micrometer/Prometheus — which task should I pick next?
