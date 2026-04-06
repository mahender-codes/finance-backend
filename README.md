# Finance Backend

A backend system for managing financial records with role-based access control. Built with Spring Boot and PostgreSQL.

## How to Run

1. Make sure you have Java 17 and PostgreSQL installed
2. Create a database named `finance_db` in PostgreSQL
3. Open `src/main/resources/application.properties` and update your credentials:

```
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Run the project from your IDE or use: `mvn spring-boot:run`
5. Server starts at `http://localhost:8080`

## How Authentication Works

There is no token-based auth in this project. Instead, every request passes two headers — `userId` (the ID of the user making the request) and `role` (their role). The system checks their role and status before allowing the action.

Example:
```
POST /api/records
userId: 1
role: ADMIN
```

## Roles

There are 3 roles in the system.

- VIEWER → can only access dashboard summary
- ANALYST → can view records and access all dashboard data
- ADMIN → full access, can manage users and records

## API Endpoints

### Users

```
POST  /api/users/registerAdmin           → register first admin (open, no headers needed)
POST  /api/users                         → create a new user (ADMIN)
GET   /api/users/all                     → get all users (ADMIN)
GET   /api/users/{userId}                → get user by id (ADMIN)
PUT   /api/users/{userId}                → update user (ADMIN)
PATCH /api/users/status/{userId}         → activate or deactivate user (ADMIN)
```

### Records

```
POST   /api/records                      → create record (ADMIN)
GET    /api/records?page=0&size=10       → get all records, paginated (ANALYST, ADMIN)
GET    /api/records/{recordId}           → get record by id (ANALYST, ADMIN)
PUT    /api/records/{recordId}           → update record (ADMIN)
DELETE /api/records/{recordId}           → soft delete record (ADMIN)
GET    /api/records/filter               → filter records (ANALYST, ADMIN)
```

Filter params (all optional): `type`, `category`, `startDate`, `endDate`

### Dashboard

```
GET /api/dashboard/summary               → total income, expenses, net balance per user (ALL roles)
GET /api/dashboard/category?userId=      → category wise totals for a user (ANALYST, ADMIN)
GET /api/dashboard/monthly               → monthly income and expense trends (ANALYST, ADMIN)
GET /api/dashboard/recent                → last 5 records by date (ANALYST, ADMIN)
```

## Sample Requests

Register first admin (no headers needed):

```
POST /api/users/registerAdmin
{
  "name": "Mahender",
  "email": "mahender@gmail.com",
  "role": "ADMIN"
}
```

Create a record:

```
POST /api/records
userId: 1
role: ADMIN
```
```json
{
  "amount": 5000.0,
  "type": "INCOME",
  "category": "Salary",
  "date": "2024-01-01",
  "notes": "January salary"
}
```

Filter records:

```
GET /api/records/filter?type=INCOME&category=Salary&startDate=2024-01-01&endDate=2024-12-31
userId: 1
role: ANALYST
```

## Assumptions

- First user must be created as ADMIN via `/api/users/registerAdmin` since there is no signup flow
- `userId` and `role` headers are used in place of token-based authentication
- Records are soft deleted — the data stays in the database but stops appearing in any queries
- All dates follow the format `yyyy-MM-dd`

## Database

PostgreSQL is used. Tables are auto-created by Hibernate on startup. Two tables are created: `users` and `financial_records`.