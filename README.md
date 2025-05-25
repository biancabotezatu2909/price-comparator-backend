# Price Comparator Backend

## Description

This Java backend application is built for the **Accesa Internship 2025 Coding Challenge**. It provides a REST API for comparing grocery prices across major Romanian supermarkets (Lidl, Kaufland, Profi). The system reads CSV files containing product and discount data, maintains a price history, and exposes intelligent endpoints to analyze shopping trends and optimize cost.

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
- JUnit 5 (Testing)
- Swagger UI (API documentation)

---

## Prerequisites

- Java 21
- Gradle 8+
- Docker

---

## Running the Application

1. **Build the project** (skip tests during build):

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
POST /api/auth/signup
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
POST /api/auth/login
```
Returns a JWT token to be used in all authorized requests.

---

## API Overview

### Price Alerts

- **Set an alert for a product** by specifying a `targetPrice`.
- The system checks daily and notifies you if the current price drops below your target.
- View all your alerts:
```http
GET /api/alerts
```

---

### Basket Optimization

Compare:
- `Worst-case`: buying everything from one store
- `Best-case`: splitting across stores
- `Savings`: percentage difference

Endpoint:
```http
POST /api/basket/optimize
```

---

### Best Discounts

```http
GET /api/discounts/best?store=lidl&category=lactate
```

- Shows current top discounts.
- Optional filters: `store`, `category`, `brand`.

---

### New Discounts

```http
GET /api/discounts/new?store=profi
```

- Discounts introduced in the last 24 hours.
- Optional filters: `store`, `category`, `brand`.

---

### Price History Graph

```http
GET /api/products/{productId}/history
```

- Returns time-series data for product price evolution.
- Usable for dynamic graph rendering in the frontend.

---

### Product Substitutes

```http
GET /api/products/{productId}/substitutes
```

- Returns similar products with better value-per-unit.



