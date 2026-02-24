# Car Park Management API

A RESTful API for managing a car park, built with Spring Boot and Java 21.

## Getting Started

### Prerequisites
- Java 21
- Maven (or use the included Maven Wrapper)

### Running the Application
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### Swagger UI

Interactive API documentation is available at:
```
http://localhost:8080/swagger-ui/index.html
```

### Running Tests
```bash
./mvnw test
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/parking` | Get available and occupied spaces |
| POST | `/parking` | Park a vehicle |
| POST | `/parking/bill` | Exit vehicle and calculate charge |

---

## Pricing

| Vehicle Type | Code | Rate per Minute |
|-------------|------|-----------------|
| Small Car | 1 | £0.10 |
| Medium Car | 2 | £0.20 |
| Large Car | 3 | £0.40 |

An additional charge of **£1.00** is applied for every 5 minutes parked.

---

## Assumptions

- The car park has **10 spaces**. This is defined as a constant in `ParkingService` and can be easily changed.
- Vehicles are charged for **complete minutes** only. Partial minutes are not charged.
- The additional £1 charge applies for each **complete 5-minute period** (e.g. 9 minutes = 1 additional charge, 10 minutes = 2 additional charges).
- Vehicle registration numbers are treated as **case-sensitive**.
- A vehicle cannot be parked twice simultaneously - no duplicate registration validation is implemented (could be added as an enhancement).
- Data is stored **in-memory** and will be lost on application restart, as per requirements.

---

## Questions I Would Have Asked

- **How many spaces does the car park have?** I assumed 10 - this should be confirmed and ideally made configurable via `application.yml`.
- **What happens if the same vehicle registration is submitted twice?** Currently the second request would park successfully in a new space. Should we validate for duplicates?
- **Should charges be rounded to 2 decimal places?** I applied standard rounding to avoid floating point precision issues.
- **Is there a maximum parking duration?** Not implemented - could be added as a business rule.

---

## Tech Stack

- Java 21
- Spring Boot 3
- SpringDoc OpenAPI (Swagger UI)
- Lombok
- JUnit 5 + AssertJ