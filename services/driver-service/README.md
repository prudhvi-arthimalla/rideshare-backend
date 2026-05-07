# Driver Service

Manages driver profiles, availability, and real-time location for the Rideshare platform.

## Responsibilities

- Tracks driver status (`AVAILABLE`, `BUSY`, `OFFLINE`)
- Stores and updates driver GPS coordinates in MongoDB
- Consumes `order.requested` events from Kafka, assigns the next available driver, and publishes `order.accepted` back

## Tech Stack

- **Spring Boot 3.5** — REST API
- **PostgreSQL** — driver profiles and status (`drivers` table)
- **MongoDB** — real-time driver locations (`driver_locations` collection)
- **Apache Kafka** — event-driven order assignment

## API

Base URL: `http://localhost:8082`  
Swagger UI: `http://localhost:8082/swagger-ui/index.html`

All endpoints require a **DRIVER** role JWT in the `Authorization: Bearer <token>` header.

| Method | Path                            | Description                                              |
|--------|---------------------------------|----------------------------------------------------------|
| POST   | `/drivers/register-availability`| Mark driver as available; creates profile if absent      |
| PUT    | `/drivers/location`             | Update current GPS coordinates                           |
| GET    | `/drivers/me`                   | Retrieve authenticated driver's profile                  |

## Kafka

| Topic             | Direction | Description                                      |
|-------------------|-----------|--------------------------------------------------|
| `order.requested` | Consumed  | Triggers driver assignment                        |
| `order.accepted`  | Published | Notifies order-service of the assigned driver ID |

## Environment Variables

| Variable                | Example                                       | Description                  |
|-------------------------|-----------------------------------------------|------------------------------|
| `DB_URL`                | `jdbc:postgresql://localhost:5432/rideshare_drivers` | PostgreSQL connection URL |
| `DB_USERNAME`           | `postgres`                                    | PostgreSQL username           |
| `DB_PASSWORD`           | `postgres`                                    | PostgreSQL password           |
| `MONGO_URI`             | `mongodb://mongo:mongo@localhost:27017`        | MongoDB connection URI        |
| `MONGO_DATABASE`        | `rideshare_drivers`                           | MongoDB database name         |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092`                            | Kafka broker address          |
| `JWT_SECRET`            | `<base64-encoded 256-bit key>`                | Must match across all services |

## Running Locally

Start infrastructure first (Postgres, MongoDB, Kafka):

```bash
cd infrastructure && docker compose up -d
```

Then start the service from the `services/` directory:

```bash
DB_URL=jdbc:postgresql://localhost:5432/rideshare_drivers \
DB_USERNAME=postgres \
DB_PASSWORD=postgres \
MONGO_URI=mongodb://mongo:mongo@localhost:27017 \
MONGO_DATABASE=rideshare_drivers \
KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
JWT_SECRET=<your-secret> \
./gradlew :driver-service:bootRun
```

## Driver Assignment Flow

```
order-service  →  order.requested  →  driver-service
                                           │
                                    finds next AVAILABLE driver
                                    marks driver BUSY (pessimistic lock)
                                           │
                                    order.accepted  →  order-service
                                                           │
                                                    sets order status = ACCEPTED
                                                    sets order.driverId
```
