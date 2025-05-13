# InternshipTask-CharityBox

Simple API managing collection boxes during fundraising events for charity organizations. The Web API was created using Spring Boot and follows the **REST architectural style**. The application is built with **Java 24**, **Hibernate** for ORM, **Maven** for dependency management, and **H2** as an in-memory database. The project  is located in the [`server`](https://github.com/WajHub/InternshipTask-CharityBox/tree/main/server).

For testing, I used **JUnit 5** and **Mockito** for Unit Tests and **MockMvc** for Integration Tests to simulate HTTP requests and verify the behavior of the API. The project also integrates an external API to fetch exchange rates from  [`https://www.exchangerate-api.com/`](https://www.exchangerate-api.com/).

- [How to run](#how-to-run)
- [How to run test](#how-to-run-tests)
- [Endpoints](#endpoints)
- [Schema Database](#Schema-Database)


## How to run [`⬆️`](#InternshipTask-CharityBox)

1. **Create .env file in the `server/src/main/resources/`  and complete content**

    ```
    API_KEY=
    ```

    >  **How to get API_KEY?** You can Generate api key here: [`https://www.exchangerate-api.com/`](https://www.exchangerate-api.com/)

2. **With Docker**
   - **Navigate to the main directory**
    ```bash
    cd InernhshipTaks-Charity
    ```
   - **Run docker**

    ```bash
    docker compose up --build
    ```

3. **Without Docker**
   - **Navigate to the main directory**
    ```bash
    cd InernhshipTaks-Charity/server
    ```
   - **Run Spring boot Application**

    ```bash
    ./mvnw spring-boot:run
    ```

The Swagger UI will be available at: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)

## How to run tests [`⬆️`](#InternshipTask-CharityBox)

1. **Navigate to the main directory**

   ```bash
   cd InernhshipTaks-Charity/server
    ```
   
   - All tests
   ```bash
   ./mvnw test
    ```
   
   - Only unit tests
   ```bash
    ./mvnw -Dtest='*UnitTests' test
    ```

   - Only integration tests
   ```bash
    ./mvnw -Dtest='*IntegrationTests' test

    ```
   
Tests are located in [`here`](https://github.com/WajHub/InternshipTask-CharityBox/tree/main/server/src/test).

## Endpoints [`⬆️`](#InternshipTask-CharityBox)

   - `GET /api/v1/events` - **Display a financial report with all fundraising events and the sum of their accounts.**

     <details>
     <summary>Example Query</summary>
     
     ```bash
     curl http://localhost:8080/api/v1/events
     ```
      </details>
     
      <details>
         <summary>Example Response</summary>

     `HTTP/1.1 200`
     ```json
     [
     {
     "uuid": "66eaa713-c6a8-47c6-98fa-da78bfab9376",
     "name": "Charity One",
     "currencyCode": "EUR",
     "balance": 261.76733
     },
     {
     "uuid": "56eb1354-a780-446f-beb9-705602f25104",
     "name": "All for hope",
     "currencyCode": "GBP",
     "balance": 0.0
     },
     {
     "uuid": "06c763c3-23c7-4d72-b46c-3d59c564656c",
     "name": "Wielka Orkiestra Świątecznej Pomocy",
     "currencyCode": "PLN",
     "balance": 901.1936
     }
     ]
     ```
      </details>



   - `POST /api/v1/events` - **Create a new fundraising event.**

      <details>
         <summary>Example Query</summary>

     ```bash
     curl -X POST http://localhost:8080/api/v1/events \
      -H "Content-Type: application/json" \
     -d '{"name": "American Red Cross", "currencyCode": "USD"}'
     ```
      </details>

      <details>
         <summary>Example Response</summary>

     `HTTP/1.1 201`
     ```json
     {
     "uuid": "546f30d1-ff13-43aa-94e3-d7666f3798d2",
     "name": "American Red Cross",
     "currencyCode": "USD",
     "balance": 0.0
     }
     ```
      </details>



   - `PUT /api/v1/events/{uuid}` - Create a new fundraising event with provided UUId to simplify testing.

      <details>
         <summary>Example Query</summary>
     
     ```bash
     curl -X PUT http://localhost:8080/api/v1/events/f9c6b673-c001-4cfe-981e-0eb58c219f6f \
      -H "Content-Type: application/json" \
     -d '{"name": "American Red Cross", "currencyCode": "USD"}'
     ```
      </details>

      <details>
         <summary>Example Response</summary>

     `HTTP/1.1 201`
     ```json
     {
     "uuid": "f9c6b673-c001-4cfe-981e-0eb58c219f6f",
     "name": "American Red Cross",
     "currencyCode": "USD",
     "balance": 0.0
     }
     ```
      </details>



   - `GET /api/v1/collections` - **List all collection boxes. Include information if the box is assigned (but don’t expose to what
     fundraising event) and if it is empty or not (but don’t expose the actual value in the box).**

      <details>
         <summary>Example Query</summary>

     ```bash
     curl http://localhost:8080/api/v1/collections
     ```
      </details>

      <details>
         <summary>Example Response</summary>

     `HTTP/1.1 200 `
     ```json
     [
     {
     "uuid": "be4c9355-bac8-4262-84f9-07cc1eb1a192",
     "isAssigned": true,
     "isEmpty": false
     },
     {
     "uuid": "5a65a78b-e765-4b26-92ea-4903124ae19c",
     "isAssigned": false,
     "isEmpty": true
     },
     {
     "uuid": "cfa73bc4-5196-4a67-bec8-20181415f21b",
     "isAssigned": true,
     "isEmpty": true
     }
     ]
     ```
      </details>
     


   - `POST /api/v1/collections` - **Create new collection box.**

      <details>
         <summary>Example Query</summary>

     ```bash
     curl -X POST http://localhost:8080/api/v1/collections
     ```
      </details>

      <details>
         <summary>Example Response</summary>

     `HTTP/1.1 201`
     ```json
     {
     "uuid": "69b073da-efbb-403c-bbcd-fbc3aaf7a4d9",
     "isAssigned": false,
     "isEmpty": true
     }
     ```
      </details>



   - `PUT /api/v1/collections/{uuid}` - Create new collection box  with provided UUId to simplify testing.

     <details>
        <summary>Example Query</summary>

     ```bash
     curl -X PUT http://localhost:8080/api/v1/collections/69b073da-efbb-403c-bbcd-fbc3aaf7a4d9
     ```
     </details>

     <details>
        <summary>Example Response</summary>

     `HTTP/1.1 201`
     ```json
     {
     "uuid": "69b073da-efbb-403c-bbcd-fbc3aaf7a4d9",
     "isAssigned": false,
     "isEmpty": true
     }
     ```
     </details>



   - `PATCH /api/v1/collections/{uuid}` - **Put (add) some money inside the collection box.**

     <details>
        <summary>Example Query</summary>

      ```bash
     curl -X PATCH http://localhost:8080/api/v1/collections/be4c9355-bac8-4262-84f9-07cc1eb1a192 \
      -H "Content-Type: application/json" \
     -d '{"currencyCode": "USD", "amount": 125.50}'
     ```
     </details>

     <details>
        <summary>Example Response</summary>

     `HTTP/1.1 200`
     ```json
     {
     "uuid": "be4c9355-bac8-4262-84f9-07cc1eb1a192",
     "isAssigned": true,
     "isEmpty": false
     }
     ```
     </details>



   - `PATCH /api/v1/events/{eventUuid}/collections/{collectionUuid}/register` - **Assign the collection box to an existing fundraising event.**

     <details>
        <summary>Example Query</summary>

      ```bash
     curl -X PATCH http://localhost:8080/api/v1/events/66eaa713-c6a8-47c6-98fa-da78bfab9376/collections/e329a2ca-d512-422d-a21c-9bbcbe034ef9/register
     ```
     </details>

     <details>
        <summary>Example Response</summary>

     `HTTP/1.1 200`
     ```json
     {
     "uuid": "e329a2ca-d512-422d-a21c-9bbcbe034ef9",
     "isAssigned": true,
     "isEmpty": true
     }
     ```
     </details>



   - `DELETE /api/v1/collections/{uuid}` - **Unregister (remove) a collection box (e.g. in case it was damaged or stolen).**

     <details>
        <summary>Example Query</summary>
     
     ```bash
     curl -X DELETE http://localhost:8080/api/v1/collections/e963b0f2c-1baf-4d41-b277-f4ab2c2e3875
     ```
     </details>

     <details>
        <summary>Example Response</summary>
     
     `HTTP/1.1 204`
     </details>



   - `PATCH /api/v1/collections/{uuid}/transfer` - **Empty the collection box i.e. “transfer” money from the box to the fundraising event’s account.**

     <details>
        <summary>Example Query</summary>

     ```bash
     curl -X PATCH http://localhost:8080/api/v1/collections/be4c9355-bac8-4262-84f9-07cc1eb1a192/transfer
     ```
     </details>

     <details>
        <summary>Example Response</summary>

     `HTTP/1.1 200`
     ```json
     {
     "uuid": "be4c9355-bac8-4262-84f9-07cc1eb1a192",
     "isAssigned": true,
     "isEmpty": true
     }
     ```
     </details>

More examples of queries you can find [`here`](https://github.com/WajHub/InternshipTask-CharityBox/blob/main/server/src/test/httpRequest/request.http).

## Schema Database [`⬆️`](#InternshipTask-CharityBox)

```mermaid
  erDiagram
   
       FUNDRAISING_EVENT {
            UUID id PK
            String name
            String currency_code "Consistent with ISO 4217"
            double balance
       }
   
       COLLECTION_BOX{
            UUID id PK
            UUID event_id FK
       }

       BOX_BALANCE_MAPPING{
            UUID box_id FK
            string currency_code
            double balance
       }
   
        FUNDRAISING_EVENT ||--o{ COLLECTION_BOX : contains
        COLLECTION_BOX ||--o{ BOX_BALANCE_MAPPING : has
```