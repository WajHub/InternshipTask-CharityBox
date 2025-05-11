# InternshipTask-CharityBox

## 🚀 How to run

1. **Create .env file and complete content**

    ```
    API_KEY=
    ```

    >  **How to get API_KEY?** You can Generate api key here: [https://www.exchangerate-api.com/](https://www.exchangerate-api.com/)

2. **Navigate to the main directory**
    ```bash
    cd InernhshipTaks-Charity
    ```

3. **Run docker**

    ```bash
    docker compose up --build
    ```
   
## Database Schema

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