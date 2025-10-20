# Patient Service API Documentation

## Base URL

```shell
http://localhost:8080/api/patients
```

## Content Type

All requests and responses use `application/json`.

## Error Responses

All errors follow this envelope:

```json
{
  "status": 400,
  "success": false,
  "error": "Bad Request",
  "message": "Validation failed"
}
```

Examples:

- Validation Error (400):

```json
{
  "status": 400,
  "success": false,
  "error": "Validation Failed",
  "message": "{full_name=Full name is required}"
}
```

- Not Found (404):

```json
{
  "status": 404,
  "success": false,
  "error": "Not Found",
  "message": "Patient not found"
}
```

## Endpoints

### 1. Create Patient

**POST** `/api/patients`

Creates a new patient record in the system.

#### Request Body

| Field       | Type   | Required | Validation              | Description                                       |
| ----------- | ------ | -------- | ----------------------- | ------------------------------------------------- |
| `full_name` | String | Yes      | NotBlank, Size(max=255) | Patient's full name                               |
| `dob`       | Date   | Yes      | NotNull, Past           | Date of birth (ISO 8601, must be in the past)     |
| `gender`    | Enum   | Yes      | NotNull                 | Must be "MALE" or "FEMALE"                        |
| `phone`     | String | Yes      | NotBlank, Pattern       | VN mobile (0xxxxxxxxx) or 1800/1900 (8-10 digits) |
| `address`   | String | Yes      | NotBlank, Size(max=255) | Patient's address                                 |
| `user_id`   | UUID   | Yes      | NotNull                 | Associated user identifier                        |

#### Example Request Body

```json
{
  "full_name": "John Doe",
  "dob": "1990-05-15T00:00:00.000Z",
  "gender": "MALE",
  "phone": "0912345678",
  "address": "123 Main Street, City, State",
  "user_id": "550e8400-e29b-41d4-a716-446655440000"
}
```

#### Response

- **Status Code:** `201 Created`
- **Body:** ApiResponse envelope

#### Example Response

```json
{
  "status": 201,
  "success": true,
  "message": "Patient created successfully",
  "data": {
    "patient_id": "550e8400-e29b-41d4-a716-446655440001",
    "full_name": "John Doe",
    "dob": "1990-05-15T00:00:00.000Z",
    "gender": "MALE",
    "phone": "0912345678",
    "address": "123 Main Street, City, State",
    "user_id": "550e8400-e29b-41d4-a716-446655440000"
  }
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
- **Body:** ApiResponse envelope

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Patient fetched successfully",
  "data": {
    "patient_id": "550e8400-e29b-41d4-a716-446655440001",
    "full_name": "John Doe",
    "dob": "1990-05-15T00:00:00.000Z",
    "gender": "MALE",
    "phone": "0912345678",
    "address": "123 Main Street, City, State",
    "user_id": "550e8400-e29b-41d4-a716-446655440000"
  }
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
- **Body:** ApiResponse with data list and pagination meta

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Patients fetched successfully",
  "data": [
    {
      "patient_id": "550e8400-e29b-41d4-a716-446655440001",
      "full_name": "John Doe",
      "dob": "1990-05-15T00:00:00.000Z",
      "gender": "MALE",
      "phone": "0912345678",
      "address": "123 Main Street, City, State",
      "user_id": "550e8400-e29b-41d4-a716-446655440000"
    }
  ],
  "meta": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPages": 1,
    "totalItems": 1
  }
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

| Field       | Type   | Required | Validation    | Description                                   |
| ----------- | ------ | -------- | ------------- | --------------------------------------------- |
| `full_name` | String | No       | Size(max=255) | Patient's full name                           |
| `dob`       | Date   | No       | Past          | Date of birth (ISO 8601, must be in the past) |
| `gender`    | Enum   | No       | -             | Must be "MALE" or "FEMALE"                    |
| `phone`     | String | No       | Pattern       | VN mobile (0xxxxxxxxx) or 1800/1900           |
| `address`   | String | No       | Size(max=255) | Patient's address                             |
| `user_id`   | UUID   | No       | -             | Associated user identifier                    |

**Note:** All fields are optional for updates. Only provided fields will be updated.

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse envelope

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Patient updated successfully",
  "data": {
    "patient_id": "550e8400-e29b-41d4-a716-446655440001",
    "full_name": "John Smith",
    "dob": "1990-05-15T00:00:00.000Z",
    "gender": "MALE",
    "phone": "09876543210",
    "address": "456 Oak Avenue, City, State",
    "user_id": "550e8400-e29b-41d4-a716-446655440000"
  }
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
- **Body:** ApiResponse envelope (no data)

```json
{
  "status": 204,
  "success": true,
  "message": "Patient deleted successfully"
}
```

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
- **Body:** ApiResponse with data list and pagination meta

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Patients fetched successfully",
  "data": [
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
  "meta": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPages": 1,
    "totalItems": 1
  }
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
  "phone": "String (valid phone number)",
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
  "phone": "String (required, valid phone number)",
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
  "phone": "String (optional, valid phone number)",
  "address": "String (optional)",
  "user_id": "UUID (optional)"
}
```

## Validation Rules

### Phone Number Format

- Must satisfy: regex pattern
- Regex:

```text
^(?:\+84|0)(?:3[2-9]|5[25689]|7[06-9]|8[1-9]|9[0-9])[0-9]{7}$|^(?:1800|1900)[0-9]{4,6}$
```

- Effective accepted formats:
  - Local mobile numbers: `0` + valid VN prefix + 8 digits (total 10)
  - Special service numbers: `1800`/`1900` + 4–6 digits (8–10 total)
- Examples: `0912345678`, `0987654321`, `0321234567`, `18001234`, `1900123456`

### Date Format

- ISO 8601 format: `YYYY-MM-DDTHH:mm:ss.sssZ`
- Must be in the past for create/update
- Example: `1990-05-15T00:00:00.000Z`

### Gender Values

- `MALE`
- `FEMALE`
