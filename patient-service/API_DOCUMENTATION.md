# Patient Service API Documentation

## Base URL

```shell
http://localhost:8080/api/patients
```

## Content Type

All requests and responses use `application/json`.

## Error Responses

### Validation Error (400 Bad Request)

```json
{
  "timestamp": "2024-01-15T10:30:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/patients"
}
```

### Not Found Error (404 Not Found)

```json
{
  "timestamp": "2024-01-15T10:30:00.000Z",
  "status": 404,
  "error": "Not Found",
  "message": "Patient not found",
  "path": "/api/patients/550e8400-e29b-41d4-a716-446655440000"
}
```

## Endpoints

### 1. Create Patient

**POST** `/api/patients`

Creates a new patient record in the system.

#### Request Body

| Field       | Type   | Required | Validation | Description                     |
| ----------- | ------ | -------- | ---------- | ------------------------------- |
| `full_name` | String | Yes      | NotBlank   | Patient's full name             |
| `dob`       | Date   | Yes      | NotNull    | Date of birth (ISO 8601 format) |
| `gender`    | Enum   | Yes      | NotNull    | Must be "MALE" or "FEMALE"      |
| `phone`     | String | Yes      | Pattern    | 10 digits starting with 0       |
| `address`   | String | Yes      | NotBlank   | Patient's address               |
| `user_id`   | UUID   | Yes      | NotNull    | Associated user identifier      |

#### Example Request Body

```json
{
  "full_name": "John Doe",
  "dob": "1990-05-15T00:00:00.000Z",
  "gender": "MALE",
  "phone": "0123456789",
  "address": "123 Main Street, City, State",
  "user_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Response

- **Status Code:** `201 Created`
- **Body:** PatientResponseDto object

#### Example Response

```json
{
  "patient_id": "550e8400-e29b-41d4-a716-446655440001",
  "full_name": "John Doe",
  "dob": "1990-05-15T00:00:00.000Z",
  "gender": "MALE",
  "phone": "0123456789",
  "address": "123 Main Street, City, State",
  "user_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### 2. Get Patient by ID

**GET** `/api/patients/{id}`

Retrieves a specific patient by their unique identifier.

#### Path Parameters

| Parameter | Type | Required | Description                 |
| --------- | ---- | -------- | --------------------------- |
| `id`      | UUID | Yes      | Patient's unique identifier |

#### Response

- **Status Code:** `200 OK`
- **Body:** PatientResponseDto object

#### Example Response

```json
{
  "patient_id": "550e8400-e29b-41d4-a716-446655440001",
  "full_name": "John Doe",
  "dob": "1990-05-15T00:00:00.000Z",
  "gender": "MALE",
  "phone": "0123456789",
  "address": "123 Main Street, City, State",
  "user_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### 3. Get All Patients

**GET** `/api/patients`

Retrieves all patients with pagination support.

#### Query Parameters

| Parameter | Type    | Required | Default | Description                           |
| --------- | ------- | -------- | ------- | ------------------------------------- |
| `page`    | Integer | No       | 0       | Page number (0-based)                 |
| `size`    | Integer | No       | 20      | Number of items per page              |
| `sort`    | String  | No       | -       | Sort criteria (e.g., "full_name,asc") |

#### Response

- **Status Code:** `200 OK`
- **Body:** Page object containing PatientResponseDto array

#### Example Response

```json
{
  "content": [
    {
      "patient_id": "550e8400-e29b-41d4-a716-446655440001",
      "full_name": "John Doe",
      "dob": "1990-05-15T00:00:00.000Z",
      "gender": "MALE",
      "phone": "0123456789",
      "address": "123 Main Street, City, State",
      "user_id": "550e8400-e29b-41d4-a716-446655440000"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 1
}
```

---

### 4. Update Patient

**PUT** `/api/patients/{id}`

Updates an existing patient record.

#### Path Parameters

| Parameter | Type | Required | Description                 |
| --------- | ---- | -------- | --------------------------- |
| `id`      | UUID | Yes      | Patient's unique identifier |

#### Request Body

| Field       | Type   | Required | Validation | Description                     |
| ----------- | ------ | -------- | ---------- | ------------------------------- |
| `full_name` | String | No       | -          | Patient's full name             |
| `dob`       | Date   | No       | -          | Date of birth (ISO 8601 format) |
| `gender`    | Enum   | No       | -          | Must be "MALE" or "FEMALE"      |
| `phone`     | String | No       | Pattern    | 10 digits starting with 0       |
| `address`   | String | No       | -          | Patient's address               |
| `user_id`   | UUID   | No       | -          | Associated user identifier      |

**Note:** All fields are optional for updates. Only provided fields will be updated.

#### Response

- **Status Code:** `200 OK`
- **Body:** Updated PatientResponseDto object

#### Example Response

```json
{
  "patient_id": "550e8400-e29b-41d4-a716-446655440001",
  "full_name": "John Smith",
  "dob": "1990-05-15T00:00:00.000Z",
  "gender": "MALE",
  "phone": "09876543210",
  "address": "456 Oak Avenue, City, State",
  "user_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### 5. Delete Patient

**DELETE** `/api/patients/{id}`

Deletes a patient record from the system.

#### Path Parameters

| Parameter | Type | Required | Description                 |
| --------- | ---- | -------- | --------------------------- |
| `id`      | UUID | Yes      | Patient's unique identifier |

#### Response

- **Status Code:** `204 No Content`
- **Body:** Empty

---

### 6. Search Patients

**GET** `/api/patients/search`

Searches for patients based on name and/or phone number with pagination support.

#### Query Parameters

| Parameter | Type    | Required | Default | Description                                |
| --------- | ------- | -------- | ------- | ------------------------------------------ |
| `name`    | String  | No       | -       | Patient name to search for (partial match) |
| `phone`   | String  | No       | -       | Phone number to search for (partial match) |
| `page`    | Integer | No       | 0       | Page number (0-based)                      |
| `size`    | Integer | No       | 20      | Number of items per page                   |

#### Response

- **Status Code:** `200 OK`
- **Body:** Page object containing matching PatientResponseDto array

#### Example Response

```json
{
  "content": [
    {
      "patient_id": "550e8400-e29b-41d4-a716-446655440001",
      "full_name": "John Doe",
      "dob": "1990-05-15T00:00:00.000Z",
      "gender": "MALE",
      "phone": "0123456789",
      "address": "123 Main Street, City, State",
      "user_id": "550e8400-e29b-41d4-a716-446655440000"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": false,
      "unsorted": true
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 1
}
```

## Data Models

### PatientResponseDto

```json
{
  "patient_id": "UUID",
  "full_name": "String",
  "dob": "Date (ISO 8601)",
  "gender": "MALE | FEMALE",
  "phone": "String (10 digits starting with 0)",
  "address": "String",
  "user_id": "UUID"
}
```

### PatientCreateRequest

```json
{
  "full_name": "String (required)",
  "dob": "Date (required, ISO 8601)",
  "gender": "MALE | FEMALE (required)",
  "phone": "String (required, 10 digits starting with 0)",
  "address": "String (required)",
  "user_id": "UUID (required)"
}
```

### PatientUpdateRequest

```json
{
  "full_name": "String (optional)",
  "dob": "Date (optional, ISO 8601)",
  "gender": "MALE | FEMALE (optional)",
  "phone": "String (optional, 10 digits starting with 0)",
  "address": "String (optional)",
  "user_id": "UUID (optional)"
}
```

## Validation Rules

### Phone Number Format

- Must match pattern: `^0[0-9]{9}$`
- Exactly 10 digits
- Must start with 0
- Examples: `0123456789`, `0987654321`

### Date Format

- ISO 8601 format: `YYYY-MM-DDTHH:mm:ss.sssZ`
- Example: `1990-05-15T00:00:00.000Z`

### Gender Values

- `MALE`
- `FEMALE`
