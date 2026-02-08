# XIS Prices Boot

REST service for the management and consultation of product prices by brand and application date.

## Description

REST API developed with Spring Boot that allows you to query product prices considering:

- **Product ID**
- **Brand ID**
- **Application Date**

The service returns the current price on the specified date, applying the temporal validity rules of price lists.

## Technologies

- **Java** 25
- **Spring Boot** 4.0.2
- **Maven** 3.9+
- **SQL** (Relational Database)
- **JUnit 5** (Testing)

## Project Structure

```
xis-prices-microservice/
├── xis-prices-boot/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/xis/prices/
│   │   │   │   ├── application/
│   │   │   │   ├── domain/
│   │   │   │   ├── infrastructure/
│   │   │   │   │   └── rest/
│   │   │   │   │       └── controller/
│   │   │   │   └── ...
│   │   │   └── resources/
│   │   │       └── application.yaml
│   │   └── test/
│   │       └── java/com/xis/prices/
│   │           └── infrastructure/rest/controller/
│   │               └── PriceApiControllerITTest.java
│   └── pom.xml
├── xis-prices-application/
│   ├── src/
│   │   ├── main/java/com/xis/prices/application/
│   │   └── test/java/
│   └── pom.xml
├── xis-prices-domain/
│   ├── src/
│   │   ├── main/java/com/xis/prices/domain/
│   │   └── test/java/
│   └── pom.xml
├── xis-prices-infrastructure/
│   ├── src/
│   │   ├── main/java/com/xis/prices/infrastructure/
│   │   └── test/java/
│   └── pom.xml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Endpoints

### GET `/api/v1/prices`

Gets the price of a product for a specific brand and date.

**Query Parameters:**

| Parameter         | Type              | Required | Description                                             |
|-------------------|-------------------|----------|---------------------------------------------------------|
| `applicationDate` | String (ISO 8601) | Yes      | Application date and time (e.g: `2020-06-14T10:00:00Z`) |
| `productId`       | Long              | Yes      | Unique product ID                                       |
| `brandId`         | Integer           | Yes      | Unique brand ID                                         |

**Request Example:**

```
GET /api/v1/prices?applicationDate=2020-06-14T10:00:00Z&productId=35455&brandId=1
Accept: application/json
```

**Successful Response (200 OK):**

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00Z",
  "endDate": "2020-12-31T23:59:59Z",
  "price": 35.50
}
```

**Not Found Response (404 Not Found):**

```json
{
  "message": "No applicable price found for productId: 35455, brandId: 10 and applicationDate: 2020-06-14T16:00Z",
  "path": "/api/v1/prices",
  "date": "2026-02-08T13:28:17.5581307+01:00"
}
```

## Installation

1. **Clone the repository:**

```bash
git clone <repository-url>
cd xis-prices-microservice
```

2. **Compile the project:**

```bash
mvn clean install
```

3. **Run the application:**

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## Testing

### Run all tests

```bash
mvn test
```

### Run specific integration tests

```bash
mvn test -Dtest=PriceApiControllerITTest
```

### Available Tests

The file `PriceApiControllerITTest.java` contains 6 integration tests:

| Test                        | Description                       | Date                 | Expected Price |
|-----------------------------|-----------------------------------|----------------------|----------------|
| test1                       | Base price applicable             | 2020-06-14T10:00:00Z | 35.50          |
| test2                       | Price with discount in time range | 2020-06-14T16:00:00Z | 25.45          |
| test3                       | Base price again                  | 2020-06-14T21:00:00Z | 35.50          |
| test4                       | Special price in date range       | 2020-06-15T10:00:00Z | 30.50          |
| test5                       | Special price from June 16th      | 2020-06-16T21:00:00Z | 38.95          |
| findPrice_whenPriceNotFound | No price exists for the date      | 2024-06-16T21:00:00Z | 404            |

## Architecture

The project follows a hexagonal architecture (Ports and Adapters):

- **Domain**: Business logic independent of the framework
- **Application**: Use cases that orchestrate domain logic
- **Infrastructure**: Implementation of adapters (REST, Database, etc.)
- **Boot**: Spring Boot configuration and entry point

## Development Notes

- The application uses reactive `WebTestClient` for integration tests
- Tests validate both HTTP status and JSON structure of responses
- Dates are handled in ISO 8601 format with UTC timezone
- Each Maven module is independent and can be compiled separately

