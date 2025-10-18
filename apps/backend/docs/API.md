# Документация API - КурКод (Backend)

Базовая информация
- Базовый URL: http://localhost:8189/
- Формат: application/json; charset=UTF-8
- Аутентификация: JWT в HttpOnly Secure cookie с именем Authorization
- Swagger UI: /swagger-ui.html
- OpenAPI JSON: /v3/api-docs


## Аутентификация (Authentication)

**POST**: `/auth/login`

Назначение: аутентификация по email и паролю. На успех устанавливается cookie Authorization.

Тело запроса:
```json
{
  "email": "user@example.com",
  "password": "secret"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 5,
    "username": "john",
    "email": "user@example.com",
    "registrationStatus": "ACTIVE",
    "lastLogin": "2024-01-01T12:34:56",
    "token": "<access-token>",
    "refreshToken": "<refresh-token>",
    "roles": [ { "id": 1, "name": "ROLE_USER" } ]
  },
  "success": true
}
```

**GET**: `/auth/refresh/token?token=<refreshToken>`

Назначение: получить новый access-токен по refresh-токену. На успех устанавливается новая cookie Authorization.

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 5,
    "username": "john",
    "email": "user@example.com",
    "registrationStatus": "ACTIVE",
    "lastLogin": "2024-01-01T12:34:56",
    "token": "<new-access-token>",
    "refreshToken": "<refresh-token>",
    "roles": [ { "id": 1, "name": "ROLE_USER" } ]
  },
  "success": true
}
```

**POST**: `/auth/register`

Назначение: регистрация нового пользователя и немедленная аутентификация; cookie Authorization устанавливается в ответе.

Тело запроса:
```json
{
  "username": "john",
  "email": "user@example.com",
  "password": "secret",
  "confirmPassword": "secret"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 6,
    "username": "john",
    "email": "user@example.com",
    "registrationStatus": "ACTIVE",
    "lastLogin": "2024-01-01T12:34:56",
    "token": "<access-token>",
    "refreshToken": "<refresh-token>",
    "roles": [ { "id": 1, "name": "ROLE_USER" } ]
  },
  "success": true
}
```


## Пользователи (Users)

**GET**: `/users/id/{userId}`

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 5,
    "username": "john",
    "email": "user@example.com",
    "role": { "id": 1, "name": "ROLE_USER" },
    "registrationStatus": "ACTIVE",
    "roles": [ { "id": 1, "name": "ROLE_USER" } ],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-02T11:00:00"
  },
  "success": true
}
```

**GET**: `/users/username/{username}`

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 5,
    "username": "john",
    "email": "user@example.com",
    "role": { "id": 1, "name": "ROLE_USER" },
    "registrationStatus": "ACTIVE",
    "roles": [ { "id": 1, "name": "ROLE_USER" } ],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-02T11:00:00"
  },
  "success": true
}
```

**POST**: `/users`

Тело запроса:
```json
{
  "username": "john",
  "password": "secret",
  "email": "user@example.com"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 7,
    "username": "john",
    "email": "user@example.com",
    "role": { "id": 1, "name": "ROLE_USER" },
    "registrationStatus": "PENDING",
    "roles": [ { "id": 1, "name": "ROLE_USER" } ],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "success": true
}
```

**PATCH**: `/users/{userId}`

Тело запроса:
```json
{
  "username": "johnny",
  "password": "new-secret",
  "email": "johnny@example.com"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 7,
    "username": "johnny",
    "email": "johnny@example.com",
    "role": { "id": 1, "name": "ROLE_USER" },
    "registrationStatus": "ACTIVE",
    "roles": [ { "id": 1, "name": "ROLE_USER" } ],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-03T12:00:00"
  },
  "success": true
}
```

**PUT**: `/users/{userId}`

Тело запроса:
```json
{
  "username": "john",
  "password": "secret",
  "email": "user@example.com"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 7,
    "username": "john",
    "email": "user@example.com",
    "role": { "id": 1, "name": "ROLE_USER" },
    "registrationStatus": "ACTIVE",
    "roles": [ { "id": 1, "name": "ROLE_USER" } ],
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-04T09:00:00"
  },
  "success": true
}
```

**DELETE**: `/users/{userId}`

Ответ: 204 No Content


## Работники (Workers)

**GET**: `/workers`

Ответ:
```json
{
  "message": "",
  "payload": [
    { "id": 1, "firstName": "Ivan", "lastName": "Ivanov" },
    { "id": 2, "firstName": "Petr", "lastName": "Petrov" }
  ],
  "success": true
}
```

**GET**: `/workers/{id}`

Ответ:
```json
{
  "message": "",
  "payload": { "id": 1, "firstName": "Ivan", "lastName": "Ivanov" },
  "success": true
}
```

**POST**: `/workers`

Тело запроса:
```json
{
  "firstName": "Ivan",
  "lastName": "Ivanov"
}
```

Ответ: 201 Created
```json
{
  "message": "",
  "payload": { "id": 3, "firstName": "Ivan", "lastName": "Ivanov" },
  "success": true
}
```

**PUT**: `/workers/{id}`

Тело запроса:
```json
{
  "firstName": "Ivan",
  "lastName": "Ivanov"
}
```

Ответ:
```json
{
  "message": "",
  "payload": { "id": 1, "firstName": "Ivan", "lastName": "Ivanov" },
  "success": true
}
```

**PATCH**: `/workers/{id}`

Тело запроса:
```json
{
  "firstName": "Ivan",
  "lastName": "Sidorov"
}
```

Ответ:
```json
{
  "message": "",
  "payload": { "id": 1, "firstName": "Ivan", "lastName": "Sidorov" },
  "success": true
}
```

**DELETE**: `/workers/{id}`

Ответ: 204 No Content


## Паспорт (Passport)

**GET**: `/workers/{workerId}/passport`

Ответ:
```json
{
  "message": "",
  "payload": {
    "series": "1234",
    "number": "567890",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-02T11:00:00"
  },
  "success": true
}
```

**POST**: `/workers/{workerId}/passport`

Тело запроса:
```json
{
  "series": "1234",
  "number": "567890"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "series": "1234",
    "number": "567890",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "success": true
}
```

**PUT**: `/workers/{workerId}/passport`

Тело запроса:
```json
{
  "series": "1234",
  "number": "567890"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "series": "1234",
    "number": "567890",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-03T12:00:00"
  },
  "success": true
}
```

**PATCH**: `/workers/{workerId}/passport`

Тело запроса:
```json
{
  "series": "1234",
  "number": "567890"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "series": "1234",
    "number": "567890",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-03T12:00:00"
  },
  "success": true
}
```

**DELETE**: `/workers/{workerId}/passport`

Ответ: 204 No Content


## Трудовой договор (Employment Contract)

**GET**: `/workers/{workerId}/contract`

Ответ:
```json
{
  "message": "",
  "payload": {
    "contractNumber": "CN-001",
    "startDate": "2024-01-01",
    "endDate": "2025-01-01",
    "position": "Developer",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-02T11:00:00"
  },
  "success": true
}
```

**POST**: `/workers/{workerId}/contract`

Тело запроса:
```json
{
  "contractNumber": "CN-001",
  "startDate": "2024-01-01",
  "endDate": "2025-01-01",
  "position": "Developer"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "contractNumber": "CN-001",
    "startDate": "2024-01-01",
    "endDate": "2025-01-01",
    "position": "Developer",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "success": true
}
```

**PUT**: `/workers/{workerId}/contract`

Тело запроса:
```json
{
  "contractNumber": "CN-001",
  "startDate": "2024-01-01",
  "endDate": "2025-01-01",
  "position": "Developer"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "contractNumber": "CN-001",
    "startDate": "2024-01-01",
    "endDate": "2025-01-01",
    "position": "Developer",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-03T12:00:00"
  },
  "success": true
}
```

**PATCH**: `/workers/{workerId}/contract`

Тело запроса:
```json
{
  "position": "Senior Developer"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "contractNumber": "CN-001",
    "startDate": "2024-01-01",
    "endDate": "2025-01-01",
    "position": "Senior Developer",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-03T12:00:00"
  },
  "success": true
}
```

**DELETE**: `/workers/{workerId}/contract`

Ответ: 204 No Content


## Штат (Staff)

**GET**: `/staff/{id}`

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 7,
    "name": "Manager"
  },
  "success": true
}
```

**GET**: `/staff`

Ответ:
```json
{
  "message": "",
  "payload": [
    { "id": 7, "name": "Manager" },
    { "id": 8, "name": "Developer" }
  ],
  "success": true
}
```

**POST**: `/staff`

Тело запроса:
```json
{
  "name": "Manager"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 9,
    "name": "Manager"
  },
  "success": true
}
```

**PUT**: `/staff/{id}`

Тело запроса:
```json
{
  "name": "Senior Manager"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 7,
    "name": "Senior Manager"
  },
  "success": true
}
```

**PATCH**: `/staff/{id}`

Тело запроса:
```json
{
  "name": "Lead"
}
```

Ответ:
```json
{
  "message": "",
  "payload": {
    "id": 7,
    "name": "Lead"
  },
  "success": true
}
```

**DELETE**: `/staff/{id}`

Ответ: 204 No Content


## Куры (Chickens)

**POST**: `/chickens`

Тело запроса:
```json
{
  "name": "Clucky",
  "age": 2
}
```

Ответ: 201 Created
```json
{
  "message": "",
  "payload": { "id": 10, "name": "Clucky", "age": 2 },
  "success": true
}
```

**GET**: `/chickens`

Ответ:
```json
{
  "message": "",
  "payload": [
    { "id": 10, "name": "Clucky", "age": 2 },
    { "id": 11, "name": "Feathers", "age": 1 }
  ],
  "success": true
}
```

**GET**: `/chickens/{id}`

Ответ:
```json
{
  "message": "",
  "payload": { "id": 10, "name": "Clucky", "age": 2 },
  "success": true
}
```

**PUT**: `/chickens/{id}`

Тело запроса:
```json
{
  "name": "Clucky",
  "age": 3
}
```

Ответ:
```json
{
  "message": "",
  "payload": { "id": 10, "name": "Clucky", "age": 3 },
  "success": true
}
```

**PATCH**: `/chickens/{id}`

Тело запроса:
```json
{
  "age": 4
}
```

Ответ:
```json
{
  "message": "",
  "payload": { "id": 10, "name": "Clucky", "age": 4 },
  "success": true
}
```

**DELETE**: `/chickens/{id}`

Ответ: 204 No Content


## Породы (Breeds)

**POST**: `/breeds`

Тело запроса:
```json
{
  "name": "Leghorn"
}
```

Ответ: 201 Created
```json
{
  "message": "",
  "payload": { "id": 3, "name": "Leghorn" },
  "success": true
}
```

**GET**: `/breeds`

Ответ:
```json
{
  "message": "",
  "payload": [
    { "id": 3, "name": "Leghorn" },
    { "id": 4, "name": "Rhode Island Red" }
  ],
  "success": true
}
```

**GET**: `/breeds/{id}`

Ответ:
```json
{
  "message": "",
  "payload": { "id": 3, "name": "Leghorn" },
  "success": true
}
```

**PUT**: `/breeds/{id}`

Тело запроса:
```json
{
  "name": "Leghorn Modified"
}
```

Ответ:
```json
{
  "message": "",
  "payload": { "id": 3, "name": "Leghorn Modified" },
  "success": true
}
```

**PATCH**: `/breeds/{id}`

Тело запроса:
```json
{
  "name": "Leghorn Updated"
}
```

Ответ:
```json
{
  "message": "",
  "payload": { "id": 3, "name": "Leghorn Updated" },
  "success": true
}
```

**DELETE**: `/breeds/{id}`

Ответ: 204 No Content
