# User Service

Handles user registration, authentication, and profile retrieval for the Rideshare platform. Built with Spring Boot 3.5, PostgreSQL, Flyway, and JWT-based stateless authentication.

---

## Endpoints

### `POST /users/register`
Registers a new user account.

- **Auth:** None
- **Body:**
```json
{
  "email": "john@example.com",
  "password": "secret123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+14155552671",
  "role": "RIDER"
}
```
- **Returns:** `201 Created` with the created user (excluding password)
- **Errors:** `400` validation failure, `409` email or phone already registered

---

### `POST /users/login`
Authenticates a user and returns a signed JWT token.

- **Auth:** None
- **Body:**
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```
- **Returns:** `200 OK`
```json
{ "token": "<jwt>" }
```
- **Errors:** `401` invalid email or password

---

### `GET /users/me`
Returns the profile of the currently authenticated user.

- **Auth:** `Authorization: Bearer <token>`
- **Returns:** `200 OK` with the user profile
- **Errors:** `401` missing or invalid token, `404` user not found

---

## Running Locally

Start infrastructure:
```bash
docker-compose -f infrastructure/docker-compose.yaml up -d
```

Set environment variables:
```bash
export JWT_SECRET=$(openssl rand -base64 32)
export DB_URL=jdbc:postgresql://localhost:5432/rideshare_users
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

Run the service:
```bash
./gradlew :user-service:bootRun
```

Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
