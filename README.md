# Price Comparator Backend

## Description

This Java backend application provides a REST API for comparing grocery prices across supermarkets. The system reads CSV files containing product and discount data, maintains a price history, and exposes intelligent endpoints to analyze shopping trends and optimize cost.

---

## Core Features

- Import product and discount data from CSV files
- Track historical price evolution per product
- Show best current discounts with optional filters
- Show newly introduced discounts in the last 24h
- Optimize shopping baskets: see best-case vs worst-case store scenarios and savings
- Price alert system: set custom price thresholds per product and receive notification if matched
- Compare value-per-unit across similar products (substitutes)
- Dynamic price history graphing data for trend visualization
- Secure login/signup functionality for accessing personalized features

---

## Technology Stack

- Java 21
- Spring Boot 3
- Gradle 8
- OpenCSV (CSV parsing)
- Docker + Docker Compose
- Swagger UI (API documentation)

---

## Prerequisites

- Java 21
- Gradle 8+
- Docker

---

## Running the Application

1. **Build the project**:

```bash
./gradlew clean build -x test
```

2. **Run with Docker Compose**:

```bash
docker-compose up --build
```

3. **Access the API documentation**:

Visit: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Authentication

### Signup

```http
POST /auth/signup
```
Request body:
```json
{
  "username": "user1",
  "password": "password123"
}
```

### Login

```http
POST /auth/login
```
Returns a JWT token to be used in all authorized requests.

---

## API Overview

### Auth Controller
- `POST /auth/signup` – Register a new user
- `POST /auth/login` – Authenticate and receive JWT

---

### Price Alert Controller
- `GET /alerts` – View all your active price alerts
- `POST /alerts` – Set an alert with target price

---

### Product Controller
- `GET /products` – List all imported products
- `GET /products/products/{productId}/price-history` – View price evolution for a product
- `GET /products/products/recommendations` – Get substitutes for better value/unit
- `GET /products/products/best-discounted` – Get currently best discounted products

---

### Shopping Basket Controller
- `POST /shopping-basket/optimize` – Optimize cost across stores vs single store purchase

---

### Discount Controller
- `GET /discounts` – List all active discounts (supports filtering)
- `GET /discounts/new` – List newly added discounts in the last 24h (supports filtering)

---

### Notification Controller
- `GET /notifications` – View all received alerts
- `POST /notifications/{id}/read` – Mark a notification as read

---

### Admin Controller
- `DELETE /admin/reset-db` – Reset all stored data (DEV/TEST only)

---

## Additional Notes

- All protected routes require a valid JWT token in the `Authorization` header.
- CSV file ingestion is handled automatically on container startup or via admin reset if configured.
- Product substitutions are based on normalized price per unit.
- Price history returns all values over time per store and is designed for frontend plotting (graph).

---
