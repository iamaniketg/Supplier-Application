# Supplier Management

## Overview

The Supplier Management is a Spring Boot application that provides endpoints for managing suppliers. It allows users to perform CRUD operations on supplier data, including adding, updating, and querying suppliers.

## Features

- Add a new supplier
- Update existing supplier details
- Query suppliers based on specific criteria
- Fetch all suppliers
- Delete supplier

## Technologies Used

- Java 21
- Spring Boot 3.x
- MongoDB
- SpringDoc
- Validation
- Lombok
- Spring web
- Maven
- JUnit 5
- Mockito
- Actuator

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.x
- A running instance of a Non-relational database (e.g., MongoDB)

### Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/iamaniketg/Supplier-Application.git
    cd Supplier-Application
    ```

2. **Configure the database:**

   Update the `application.properties` file located in the `src/main/resources` directory with your database connection details:

    ```bash
   properties
   spring.data.mongodb.host=localhost
   spring.data.mongodb.port=27017
   spring.data.mongodb.database=makerSharks_db
   # Set Custom Context Path
   server.servlet.context-path=/api/supplier
   Actuator Endpoints Configuration
   management.endpoints.web.exposure.include=health,info
   management.endpoint.health.show-details=always
   management.endpoint.health.show-components=always
   management.security.enabled=false
   server.port=7575
    ```

3. **Build the project:**

    ```bash
    mvn clean install
    ```

4. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

## API Endpoints

### Add a Supplier

- **URL:** `/api/supplier/add`
- **Method:** `POST`
- **Request Body:** `AddSupplierDTO`
- **Response:** `200 OK` with the message "Supplier added successfully"
- **Curl:** `curl -X 'POST' \
  'http://localhost:7575/api/supplier/add' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "companyName": "op corp",
  "website": "op.com",
  "location": "Hyderabad",
  "natureOfBusiness": "SMALL_SCALE",
  "manufacturingProcesses": [
  "MOULDING"
  ]
  }'`

### Update a Supplier

- **URL:** `/api/supplier/{supplierId}`
- **Method:** `PATCH`
- **Path Variable:** `supplierId` - The ID of the supplier to update
- **Request Body:** `UpdateSupplierDTO`
- **Response:** `200 OK` with the updated `Supplier` object
- **Curl:** `curl -X 'PATCH' \
  'http://localhost:7575/api/supplier/66c72bdfe2ef51140e19f467' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "companyName": "ABC CORP"
  }'`

### Query Suppliers

- **URL:** `/api/supplier/query`
- **Method:** `POST`
- **Query Parameters:**
    - `location` (optional)
    - `natureOfBusiness` (optional)
    - `manufacturingProcess` (optional)
    - `page` (default `0`)
    - `size` (default `10`)
- **Response:** `200 OK` with a list of `Supplier` objects
- **Curl:** `curl -X 'POST' \
  'http://localhost:7575/api/supplier/query?location=Pune&natureOfBusiness=SMALL_SCALE&manufacturingProcess=MOULDING&page=1&size=10' \
  -H 'accept: */*' \
  -d ''`

### Get All Suppliers

- **URL:** `/api/supplier/all`
- **Method:** `GET`
- **Response:** `200 OK` with a list of all `Supplier` objects
- **Curl:** `curl -X 'GET' \
  'http://localhost:7575/api/supplier/all' \
  -H 'accept: */*`

### Delete Supplier by Id
- **URL:** `/api/supplier/{supplierId}`
- **Method:** `DELETE`
- **Path Variable:** `supplierId` - The ID of the supplier to update
- **Response:** `200 OK` with a list of all `Supplier` objects
- **Curl:** `curl -X 'DELETE' \
  'http://localhost:7575/api/supplier/66c8742588dcfe6f3198f29e' \
  -H 'accept: */*'`

## Testing

### Running Unit Tests

The application uses JUnit 5 and Mockito for unit testing.

```bash
mvn test
