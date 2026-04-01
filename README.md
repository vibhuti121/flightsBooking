# Flights Booking System

This project is a Spring Boot MVC flight booking system with in-memory storage only.
It uses:

- Spring Boot
- Spring Web
- Lombok
- Java 17
- Maven Wrapper
- In-memory repositories backed by `Map` and `AtomicLong`

No database, JPA, H2, or persistence dependency is used.

## Required Modules

Install or make sure the following are available:

- Java 17
- Maven 3.9+ or use the included Maven Wrapper `./mvnw`

To verify:

```bash
java -version
./mvnw -version
```

## Process To Run The Application

Build and run:

```bash
./mvnw spring-boot:run
```

Alternative:

```bash
./mvnw clean package
java -jar target/flightsBooking-0.0.1-SNAPSHOT.jar
```

Default server:

- Base URL: `http://localhost:8080`

The application seeds sample flights at startup.

## API Endpoints

All successful requests return HTTP `200 OK`.
Error handling rules:

- HTTP `400 Bad Request` for wrong parameters, missing parameters, invalid request body, unavailable seats, invalid meals, or invalid travel date.
- HTTP `400 Bad Request` for wrong parameters, missing parameters, invalid request body, unavailable seats, invalid meals, invalid past travel date, or no matching flights for the requested search.
- HTTP `500 Internal Server Error` for unexpected server-side failures.

### 1. Get Available Flights

Fetch all flights or filter by origin, destination, and travel date.

```bash
curl --location 'http://localhost:8080/available/flights'
```

With filters:

```bash
curl --location 'http://localhost:8080/available/flights?origin=DEL&destination=BLR&travelDate=2026-04-15'
```

Success:

- `200 OK` with matching flights.

Wrong case:

```bash
curl --location 'http://localhost:8080/available/flights?travelDate=wrong-date'
```

- Returns `400 Bad Request`

Wrong date case:

```bash
curl --location 'http://localhost:8080/available/flights?origin=DEL&destination=BLR&travelDate=2026-03-31'
```

- Returns `400 Bad Request`
- Example message: `Travel date cannot be in the past. Please provide a valid future date.`

No flights found case:

```bash
curl --location 'http://localhost:8080/available/flights?origin=DEL&destination=MAA&travelDate=2026-04-15'
```

- Returns `400 Bad Request`
- Example message: `No flights found for origin DEL, destination MAA on date 2026-04-15.`

### 2. Review Price

Fetch base price and cancellation/modification details for a flight.

```bash
curl --location 'http://localhost:8080/review/price?flightId=1'
```

Success:

- `200 OK` with base price, cancellation policy, modification policy, and available seats.

Wrong case:

```bash
curl --location 'http://localhost:8080/review/price?flightId=999'
```

- Returns `400 Bad Request`

### 3. Get Available Seats

Fetch seats that are still available for the selected flight.

```bash
curl --location 'http://localhost:8080/avaialble/seats?flightId=1'
```

Success:

- `200 OK` with available seats for the flight.

Wrong case:

```bash
curl --location 'http://localhost:8080/avaialble/seats'
```

- Returns `400 Bad Request`

### 4. Get Available Meals

Fetch meals available for the selected flight.

```bash
curl --location 'http://localhost:8080/avaialble/meals?flightId=1'
```

Success:

- `200 OK` with available meals for the flight.

Wrong case:

```bash
curl --location 'http://localhost:8080/avaialble/meals?flightId=999'
```

- Returns `400 Bad Request`

### 5. Get Bookings

Fetch all confirmed bookings.

```bash
curl --location 'http://localhost:8080/bookings'
```

Success:

- `200 OK` with all current bookings.

### 6. Confirm Booking

Confirm a booking and mark selected seats as unavailable.

```bash
curl --location 'http://localhost:8080/confirm/booking' \
--header 'Content-Type: application/json' \
--data '{
  "flightId": 1,
  "seatSelections": {
    "P1": "1A",
    "P2": "1B"
  },
  "passengers": [
    {
      "paxId": "P1",
      "name": "Alice"
    },
    {
      "paxId": "P2",
      "name": "Bob"
    }
  ],
  "customerMail": "customer@test.com",
  "customerNumber": "9999999999",
  "mealSelections": {
    "P1": ["VGML"],
    "P2": ["CHML"]
  },
  "travelDate": "2026-04-15"
}'
```

Success:

- `200 OK` with created booking details.

Wrong case:

```bash
curl --location 'http://localhost:8080/confirm/booking' \
--header 'Content-Type: application/json' \
--data '{
  "flightId": 1,
  "seatSelections": {
    "P1": "9Z"
  },
  "passengers": [
    {
      "paxId": "P1",
      "name": "Alice"
    }
  ],
  "customerMail": "customer@test.com",
  "customerNumber": "9999999999",
  "mealSelections": {},
  "travelDate": "2026-04-15"
}'
```

- Returns `400 Bad Request`

Wrong body format case:

```bash
curl --location 'http://localhost:8080/confirm/booking' \
--header 'Content-Type: application/json' \
--data '{
  "flightId": "abc"
}'
```

- Returns `400 Bad Request`

Server error case:

- Any unhandled runtime failure returns `500 Internal Server Error`.
- Response body includes `timestamp`, `status`, `error`, `message`, and `path`.

## Error Response Examples

Example `400 Bad Request` response for no flights found:

```json
{
  "timestamp": "2026-04-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "No flights found for origin DEL, destination MAA on date 2026-04-15.",
  "path": "/available/flights"
}
```

Example `400 Bad Request` response for wrong date:

```json
{
  "timestamp": "2026-04-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Travel date cannot be in the past. Please provide a valid future date.",
  "path": "/available/flights"
}
```

Example `500 Internal Server Error` response:

```json
{
  "timestamp": "2026-04-01T10:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Unexpected server error occurred.",
  "path": "/confirm/booking"
}
```

## Flow Of Application

### High-Level Flow

1. Application starts and seeds sample flight data in memory.
2. User fetches available flights using `/available/flights`.
3. User reviews price, cancellation policy, and modification policy using `/review/price`.
4. User checks seats and meals using `/avaialble/seats` and `/avaialble/meals`.
5. User confirms booking through `/confirm/booking`.
6. Booking service validates passengers, seat mappings, seat availability, and meals.
7. Booking is stored in memory and booked seats are marked unavailable in the flight.
8. User can fetch all bookings using `/bookings`.

### MVC Flow

- Controller:
  Receives HTTP requests and returns API responses.

- Service:
  Applies business rules like seat validation, booking confirmation, and availability checks.

- Repository:
  Stores flights and bookings in memory using `ConcurrentHashMap` and `AtomicLong`.

- Model/DTO:
  Represents flight, seat, meal, booking, passenger, and API request/response data.

## In-Memory Storage Design

- Flights are stored in an in-memory map.
- Bookings are stored in a separate in-memory map.
- IDs are generated using `AtomicLong`.
- Data resets whenever the application restarts.

## Notes

- Endpoint names `avaialble/seats` and `avaialble/meals` intentionally match the requested API contract.
- Sample flights are preloaded for testing the APIs quickly.
- Since storage is in memory, no booking data is persisted after shutdown.
- Resource lookups with invalid ids are treated as client input errors and return `400`.
