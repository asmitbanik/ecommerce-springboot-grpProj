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

## Run locally (no Docker required)

This project can be run entirely on your local machine without Docker. You need the following installed:

- **Java 21 (JDK)**
- **Maven**
- **MongoDB** running on `mongodb://localhost:27017` (default) — install MongoDB Community on Windows or use WSL
- **Redis** running on `localhost:6379` (required for rate limiting)

Steps to run locally:

1. Ensure MongoDB and Redis are running locally.
2. Configure optional environment variables if you want a custom admin user:
   - `ADMIN_USERNAME`, `ADMIN_PASSWORD`, `ADMIN_EMAIL` (defaults: `admin` / `admin123` / `admin@example.com`)
3. Build and run with Maven:

```powershell
# from the project root (Windows PowerShell)
mvn clean package
java -jar target/ecommerce-backend-0.0.1-SNAPSHOT.jar
```

Or run directly with Spring Boot:

```powershell
mvn spring-boot:run
```

4. The app will be available at http://localhost:8080

## Running tests
- Unit tests: `mvn test`
  - Unit test summaries are written as JUnit tests under `src/test/java`. For example, `ProductServiceTest` verifies create and update error handling for products.
- Integration tests that exercise MongoDB/Redis require those services to be running locally; tests are written to assume local services (no Docker/Testcontainers is required).

## Services overview
Core service responsibilities are documented in `docs/services.md`. This includes a concise description of `ProductService` behavior (pagination, caching, image uploads) and guidance for expected inputs and errors.

## Quick API checks (after app is up)
- Register: POST /api/auth/register {"username":"user","email":"u@example.com","password":"pass"}
- Login: POST /api/auth/login {"username":"admin","password":"admin123"}
- Create product (admin): POST /api/products (Bearer token)
- Create payment: POST /api/payments/create {"amount":100, "orderId":"ord1"}
- Webhook: POST /api/payments/webhook (HMAC-SHA256 signature header `X-Razorpay-Signature`)

## CI
A GitHub Actions workflow is included at `.github/workflows/ci.yml` to run the test suite on push. The workflow uses container services on GitHub Actions, but you do not need Docker locally to run the app or tests.

## Quick API checks (after app is up)
- Register: POST /api/auth/register {"username":"user","email":"u@example.com","password":"pass"}
- Login: POST /api/auth/login {"username":"admin","password":"admin123"}
- Create product (admin): POST /api/products (Bearer token)
- Create payment: POST /api/payments/create {"amount":100, "orderId":"ord1"}
- Webhook: POST /api/payments/webhook (HMAC-SHA256 signature header `X-Razorpay-Signature`)

## CI
A GitHub Actions workflow is included at `.github/workflows/ci.yml` to run the test suite on push/PR.

