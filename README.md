# Finance Backend

A backend system for managing financial records with role-based access control.
Built with Spring Boot and PostgreSQL.

## How to Run

1. Make sure you have Java 17 and PostgreSQL installed
2. Create a database named `finance_db` in PostgreSQL
3. Open `src/main/resources/application.properties` and update your credentials.

spring.datasource.username=your_username
spring.datasource.password=your_password

4. Run the project from your IDE or use:
mvn spring-boot:run

5. Server starts at http://localhost:8080

## How Authentication Works

There is no token-based auth in this project. Instead, every request takes a `requestedBy` parameter which is the ID of the user making the request. The system checks their role and status before allowing the action.

Example: POST /api/records?requestedBy=1

## Roles

There are 3 roles in the system.

VIEWER  → can only access dashboard summary.

ANALYST → can view records and access all dashboard data.

ADMIN   → full access, can manage users and records.

## API Endpoints

### Users

POST   /api/users                        → create a new user (open)

GET    /api/users?requestedBy=           → get all users (ADMIN only)

GET    /api/users/{userId}?requestedBy=  → get user by id (ADMIN only)

PUT    /api/users/{userId}?requestedBy=  → update user (ADMIN only)

PATCH  /api/users/status/{userId}?requestedBy= → activate or deactivate user (ADMIN only)

### Records

POST   /api/records?requestedBy=                          → create record (ANALYST, ADMIN)

GET    /api/records?requestedBy=                          → get all records (ANALYST, ADMIN)

GET    /api/records/{recordId}?requestedBy=               → get record by id (ANALYST, ADMIN)

PUT    /api/records/{recordId}?requestedBy=               → update record (ADMIN only)

DELETE /api/records/{recordId}?requestedBy=               → delete record (ADMIN only)

GET    /api/records/filter?requestedBy=&type=&category=&startDate=&endDate=  → filter records (ANALYST, ADMIN)

### Dashboard

GET /api/dashboard/summary?requestedBy=          → total income, expenses, net balance per user (ALL roles)

GET /api/dashboard/category?userId=&requestedBy= → category wise totals for a user (ANALYST, ADMIN)

GET /api/dashboard/monthly?requestedBy=          → monthly income and expense trends (ANALYST, ADMIN)

GET /api/dashboard/recent?requestedBy=           → last 5 records by date (ANALYST, ADMIN)

## Sample Requests
```
Create a user-
POST /api/users
{
  "name": "Mahender",
  "email": "mahender@gmail.com",
  "role": "ADMIN"
}

Create a record-
POST /api/records?requestedBy=1
{
  "amount": 5000.0,
  "type": "INCOME",
  "category": "Salary",
  "date": "2024-01-01",
  "notes": "January salary"
}
```
Filter records-
GET /api/records/filter?requestedBy=1&type=INCOME&category=Salary&startDate=2024-01-01&endDate=2024-12-31

## Assumptions

- First user must be created as ADMIN manually since there is no signup flow
- requestedBy is used in place of token-based authentication
- Records are permanently deleted, soft delete is not implemented
- All dates follow the format yyyy-MM-dd

## Database

PostgreSQL is used. Tables are auto-created by Hibernate on startup.
Two tables are created: users and financial_records.