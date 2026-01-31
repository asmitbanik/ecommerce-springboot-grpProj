# E-commerce Backend (Spring Boot)

This project is a Spring Boot backend for an E-commerce system with the following features required by the exercise:

- Spring Boot + MongoDB
- JWT authentication with role-based access control
- REST APIs (CRUD for products, file upload for product images)
- Pagination, sorting, filtering
- Caching (Caffeine)
- Simple in-memory API rate limiting
- Validation (Jakarta Validation)
- Global exception handling
- Swagger / OpenAPI (springdoc)
- Mock email service (configured via `spring.mail` properties)

## Quick start

Requirements:
- Java 21
- Maven
- MongoDB running locally on default port

Run:

mvn spring-boot:run

Open API docs: http://localhost:8080/swagger-ui.html

## Payments (mock Razorpay integration)

- Endpoint to create a mock payment: POST /api/payments/create (body: amount, orderId, currency)
- Webhook endpoint to process provider events: POST /api/payments/webhook

## Next steps (TODO)
- Added unit tests for Payment, Product and HMAC utils
- Added caching with TTL and cache invalidation on create/update/delete
- Added analytics APIs (product views, sales aggregates)
- Switched to Redis-backed distributed rate limiting (requires Redis running)

## Notes
- Redis is required for the distributed rate limiter and recommended for analytics caching. Configure `spring.redis.host` and `spring.redis.port` in `application.yml`.
- Payment webhook now verifies HMAC-SHA256 signature using `razorpay.secret` (set in `application.yml`).

## Run locally with Docker (recommended)
1. Ensure Docker Desktop (or Docker Engine) is running on your machine (you must have a running Docker daemon).
2. Build & start services (MongoDB, Redis, app):

```bash
cd ecommerce-backend
docker compose up --build -d
```

3. The app will be available at http://localhost:8080

Environment variables (optional):
- `ADMIN_USERNAME`, `ADMIN_PASSWORD`, `ADMIN_EMAIL` — used to create an initial admin user at start (defaults: `admin` / `admin123`).

## Running tests locally (if you don't have local Maven installed)
You can run the test suite inside a Maven container (Docker):

```powershell
# from the project root (Windows PowerShell)
docker run --rm -v ${PWD}:/workspace -w /workspace maven:3.9-eclipse-temurin-21 mvn -q test
```

Integration tests use Testcontainers and therefore require Docker to be running on your machine.

Or, if you have Maven installed locally:

mvn test

## Quick API checks (after app is up)
- Register: POST /api/auth/register {"username":"user","email":"u@example.com","password":"pass"}
- Login: POST /api/auth/login {"username":"admin","password":"admin123"}
- Create product (admin): POST /api/products (Bearer token)
- Create payment: POST /api/payments/create {"amount":100, "orderId":"ord1"}
- Webhook: POST /api/payments/webhook (HMAC-SHA256 signature header `X-Razorpay-Signature`)

## CI
A GitHub Actions workflow is included at `.github/workflows/ci.yml` to run the test suite on push/PR.

