## Appointment Service API Documentation

### Base URL

```shell
http://localhost:8080/api/appointments
```

### Content Type

All requests and responses use `application/json`.

### Error Responses

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
  "message": "{start_time=must be a future date, end_time=must be a future date}"
}
```

- Not Found (404):

```json
{
  "status": 404,
  "success": false,
  "error": "Not Found",
  "message": "Appointment not found"
}
```

- Conflict Error (400):

```json
{
  "status": 400,
  "success": false,
  "error": "Bad Request",
  "message": "Time slot conflicts with an existing confirmed appointment"
}
```

- Time Validation Error (400):

```json
{
  "status": 400,
  "success": false,
  "error": "Bad Request",
  "message": "start_time must be before end_time"
}
```

---

## Endpoints

### 1. Create Appointment

**POST** `/api/appointments`

Creates a new appointment.

#### Request Body

| Field        | Type      | Required | Validation      | Description                  |
| ------------ | --------- | -------- | --------------- | ---------------------------- |
| `patient_id` | UUID      | Yes      | NotNull         | Patient identifier           |
| `doctor_id`  | UUID      | No       | -               | Doctor identifier (optional) |
| `clinic_id`  | UUID      | Yes      | NotNull         | Clinic identifier            |
| `start_time` | Date-Time | Yes      | NotNull, Future | ISO 8601 start time          |
| `end_time`   | Date-Time | Yes      | NotNull, Future | ISO 8601 end time            |

#### Example Request Body

```json
{
  "patient_id": "550e8400-e29b-41d4-a716-446655440000",
  "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
  "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
  "start_time": "2025-12-01T09:00:00Z",
  "end_time": "2025-12-01T09:30:00Z"
}
```

#### Response

- **Status Code:** `201 Created`
- **Body:** ApiResponse envelope with `AppointmentResponseDto`

#### Example Response

```json
{
  "status": 201,
  "success": true,
  "message": "Appointment created successfully",
  "data": {
    "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
    "patient_id": "550e8400-e29b-41d4-a716-446655440000",
    "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
    "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
    "start_time": "2025-12-01T09:00:00Z",
    "end_time": "2025-12-01T09:30:00Z",
    "status": "PENDING"
  }
}
```

---

### 2. Get Appointment by ID

**GET** `/api/appointments/{id}`

Retrieves a specific appointment.

#### Path Parameters

| Parameter | Type | Required | Description                   |
| --------- | ---- | -------- | ----------------------------- |
| `id`      | UUID | Yes      | Appointment unique identifier |

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse envelope with `AppointmentResponseDto`

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Appointment fetched successfully",
  "data": {
    "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
    "patient_id": "550e8400-e29b-41d4-a716-446655440000",
    "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
    "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
    "start_time": "2025-12-01T09:00:00Z",
    "end_time": "2025-12-01T09:30:00Z",
    "status": "PENDING"
  }
}
```

---

### 3. Get All Appointments

**GET** `/api/appointments`

Retrieves all appointments with optional filtering and pagination support.

#### Query Parameters

| Parameter    | Type      | Required | Default | Description                                             |
| ------------ | --------- | -------- | ------- | ------------------------------------------------------- |
| `patient_id` | UUID      | No       | -       | Filter by patient                                       |
| `doctor_id`  | UUID      | No       | -       | Filter by doctor                                        |
| `clinic_id`  | UUID      | No       | -       | Filter by clinic                                        |
| `status`     | Enum      | No       | -       | One of `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED` |
| `start_from` | Date-Time | No       | -       | Start time from (ISO 8601)                              |
| `start_to`   | Date-Time | No       | -       | Start time to (ISO 8601)                                |
| `end_from`   | Date-Time | No       | -       | End time from (ISO 8601)                                |
| `end_to`     | Date-Time | No       | -       | End time to (ISO 8601)                                  |
| `page`       | Integer   | No       | 0       | Page number (0-based)                                   |
| `size`       | Integer   | No       | 20      | Items per page                                          |
| `sort`       | String    | No       | -       | Sort criteria (e.g., `start_time,desc`)                 |

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse with data list and pagination meta

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Appointments fetched successfully",
  "data": [
    {
      "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
      "patient_id": "550e8400-e29b-41d4-a716-446655440000",
      "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
      "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
      "start_time": "2025-12-01T09:00:00Z",
      "end_time": "2025-12-01T09:30:00Z",
      "status": "PENDING"
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

### 4. Search Appointments

**GET** `/api/appointments/search`

Searches for appointments by filters with pagination.

**Note:** This endpoint provides the same functionality as the main GET `/api/appointments` endpoint. Both endpoints accept the same query parameters and return identical results.

#### Query Parameters

| Parameter    | Type      | Required | Default | Description                                             |
| ------------ | --------- | -------- | ------- | ------------------------------------------------------- |
| `patient_id` | UUID      | No       | -       | Filter by patient                                       |
| `doctor_id`  | UUID      | No       | -       | Filter by doctor                                        |
| `clinic_id`  | UUID      | No       | -       | Filter by clinic                                        |
| `status`     | Enum      | No       | -       | One of `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED` |
| `start_from` | Date-Time | No       | -       | Start time from (ISO 8601)                              |
| `start_to`   | Date-Time | No       | -       | Start time to (ISO 8601)                                |
| `end_from`   | Date-Time | No       | -       | End time from (ISO 8601)                                |
| `end_to`     | Date-Time | No       | -       | End time to (ISO 8601)                                  |
| `page`       | Integer   | No       | 0       | Page number (0-based)                                   |
| `size`       | Integer   | No       | 20      | Items per page                                          |
| `sort`       | String    | No       | -       | Sort criteria (e.g., `start_time,desc`)                 |

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse with data list and pagination meta

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Appointments fetched successfully",
  "data": [
    {
      "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
      "patient_id": "550e8400-e29b-41d4-a716-446655440000",
      "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
      "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
      "start_time": "2025-12-01T09:00:00Z",
      "end_time": "2025-12-01T09:30:00Z",
      "status": "PENDING"
    },
    {
      "appointment_id": "550e8400-e29b-41d4-a716-446655440011",
      "patient_id": "550e8400-e29b-41d4-a716-446655440003",
      "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
      "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
      "start_time": "2025-12-01T10:00:00Z",
      "end_time": "2025-12-01T10:30:00Z",
      "status": "CONFIRMED"
    }
  ],
  "meta": {
    "currentPage": 0,
    "pageSize": 20,
    "totalPages": 1,
    "totalItems": 2
  }
}
```

---

### 5. Check Doctor Availability

**GET** `/api/appointments/availability/doctor`

Checks doctor availability for a time window.

#### Query Parameters

| Parameter    | Type      | Required | Description         |
| ------------ | --------- | -------- | ------------------- |
| `clinic_id`  | UUID      | Yes      | Clinic identifier   |
| `doctor_id`  | UUID      | Yes      | Doctor identifier   |
| `start_time` | Date-Time | Yes      | ISO 8601 start time |
| `end_time`   | Date-Time | Yes      | ISO 8601 end time   |

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse with boolean `data`

```json
{
  "status": 200,
  "success": true,
  "message": "Availability checked successfully",
  "data": true
}
```

---

### 6. Update Appointment

**PUT** `/api/appointments/{id}`

Updates an existing appointment.

#### Path Parameters

| Parameter | Type | Required | Description                   |
| --------- | ---- | -------- | ----------------------------- |
| `id`      | UUID | Yes      | Appointment unique identifier |

#### Request Body

| Field        | Type      | Required | Validation | Description         |
| ------------ | --------- | -------- | ---------- | ------------------- |
| `doctor_id`  | UUID      | No       | -          | Doctor identifier   |
| `clinic_id`  | UUID      | No       | -          | Clinic identifier   |
| `start_time` | Date-Time | No       | Future     | ISO 8601 start time |
| `end_time`   | Date-Time | No       | Future     | ISO 8601 end time   |

Note: All fields are optional; only provided fields will be updated.

#### Example Request Body

```json
{
  "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
  "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
  "start_time": "2025-12-01T14:00:00Z",
  "end_time": "2025-12-01T14:30:00Z"
}
```

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse envelope with `AppointmentResponseDto`

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Appointment updated successfully",
  "data": {
    "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
    "patient_id": "550e8400-e29b-41d4-a716-446655440000",
    "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
    "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
    "start_time": "2025-12-01T14:00:00Z",
    "end_time": "2025-12-01T14:30:00Z",
    "status": "PENDING"
  }
}
```

---

### 7. Reschedule Appointment

**POST** `/api/appointments/{id}/reschedule`

Reschedules an appointment.

#### Path Parameters

| Parameter | Type | Required | Description                   |
| --------- | ---- | -------- | ----------------------------- |
| `id`      | UUID | Yes      | Appointment unique identifier |

#### Request Body

| Field        | Type      | Required | Validation      | Description               |
| ------------ | --------- | -------- | --------------- | ------------------------- |
| `doctor_id`  | UUID      | No       | -               | New doctor (optional)     |
| `start_time` | Date-Time | Yes      | NotNull, Future | New start time (ISO 8601) |
| `end_time`   | Date-Time | Yes      | NotNull, Future | New end time (ISO 8601)   |

#### Example Request Body

```json
{
  "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
  "start_time": "2025-12-02T09:00:00Z",
  "end_time": "2025-12-02T09:30:00Z"
}
```

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse envelope with `AppointmentResponseDto`

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Appointment rescheduled successfully",
  "data": {
    "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
    "patient_id": "550e8400-e29b-41d4-a716-446655440000",
    "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
    "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
    "start_time": "2025-12-02T09:00:00Z",
    "end_time": "2025-12-02T09:30:00Z",
    "status": "PENDING"
  }
}
```

---

### 8. Update Appointment Status

**PATCH** `/api/appointments/{id}/status`

Updates appointment status.

#### Path Parameters

| Parameter | Type | Required | Description                   |
| --------- | ---- | -------- | ----------------------------- |
| `id`      | UUID | Yes      | Appointment unique identifier |

#### Request Body

| Field    | Type | Required | Validation | Description                                      |
| -------- | ---- | -------- | ---------- | ------------------------------------------------ |
| `status` | Enum | Yes      | NotNull    | `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED` |

#### Example Request Body

```json
{
  "status": "CONFIRMED"
}
```

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse envelope with `AppointmentResponseDto`

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Appointment status updated successfully",
  "data": {
    "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
    "patient_id": "550e8400-e29b-41d4-a716-446655440000",
    "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
    "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
    "start_time": "2025-12-01T09:00:00Z",
    "end_time": "2025-12-01T09:30:00Z",
    "status": "CONFIRMED"
  }
}
```

---

### 9. Quick Status Actions

Change status without body.

- **POST** `/api/appointments/{id}/confirm` → set to `CONFIRMED`
- **POST** `/api/appointments/{id}/cancel` → set to `CANCELLED`
- **POST** `/api/appointments/{id}/complete` → set to `COMPLETED`

#### Path Parameters

| Parameter | Type | Required | Description                   |
| --------- | ---- | -------- | ----------------------------- |
| `id`      | UUID | Yes      | Appointment unique identifier |

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse envelope with `AppointmentResponseDto`

#### Example Response (Confirm)

```json
{
  "status": 200,
  "success": true,
  "message": "Appointment confirmed successfully",
  "data": {
    "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
    "patient_id": "550e8400-e29b-41d4-a716-446655440000",
    "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
    "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
    "start_time": "2025-12-01T09:00:00Z",
    "end_time": "2025-12-01T09:30:00Z",
    "status": "CONFIRMED"
  }
}
```

#### Example Response (Cancel)

```json
{
  "status": 200,
  "success": true,
  "message": "Appointment cancelled successfully",
  "data": {
    "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
    "patient_id": "550e8400-e29b-41d4-a716-446655440000",
    "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
    "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
    "start_time": "2025-12-01T09:00:00Z",
    "end_time": "2025-12-01T09:30:00Z",
    "status": "CANCELLED"
  }
}
```

#### Example Response (Complete)

```json
{
  "status": 200,
  "success": true,
  "message": "Appointment completed successfully",
  "data": {
    "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
    "patient_id": "550e8400-e29b-41d4-a716-446655440000",
    "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
    "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
    "start_time": "2025-12-01T09:00:00Z",
    "end_time": "2025-12-01T09:30:00Z",
    "status": "COMPLETED"
  }
}
```

---

### 10. Delete Appointment

**DELETE** `/api/appointments/{id}`

Deletes an appointment.

#### Path Parameters

| Parameter | Type | Required | Description                   |
| --------- | ---- | -------- | ----------------------------- |
| `id`      | UUID | Yes      | Appointment unique identifier |

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse envelope indicating deletion

```json
{
  "status": 204,
  "success": true,
  "message": "Appointment deleted successfully"
}
```

---

### 11. List Appointments by Patient

**GET** `/api/appointments/patients/{patientId}/appointments`

Lists appointments for a patient with pagination.

#### Path Parameters

| Parameter   | Type | Required | Description        |
| ----------- | ---- | -------- | ------------------ |
| `patientId` | UUID | Yes      | Patient identifier |

#### Query Parameters

Uses standard pagination (`page`, `size`, `sort`).

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse with data list and pagination meta

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Appointments fetched successfully",
  "data": [
    {
      "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
      "patient_id": "550e8400-e29b-41d4-a716-446655440000",
      "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
      "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
      "start_time": "2025-12-01T09:00:00Z",
      "end_time": "2025-12-01T09:30:00Z",
      "status": "PENDING"
    },
    {
      "appointment_id": "550e8400-e29b-41d4-a716-446655440011",
      "patient_id": "550e8400-e29b-41d4-a716-446655440000",
      "doctor_id": "550e8400-e29b-41d4-a716-446655440003",
      "clinic_id": "550e8400-e29b-41d4-a716-446655440004",
      "start_time": "2025-12-02T10:00:00Z",
      "end_time": "2025-12-02T10:30:00Z",
      "status": "CONFIRMED"
    }
  ],
  "meta": {
    "currentPage": 0,
    "pageSize": 20,
    "totalPages": 1,
    "totalItems": 2
  }
}
```

---

### 12. List Appointments by Doctor

**GET** `/api/appointments/doctors/{doctorId}/appointments`

Lists appointments for a doctor with pagination.

#### Path Parameters

| Parameter  | Type | Required | Description       |
| ---------- | ---- | -------- | ----------------- |
| `doctorId` | UUID | Yes      | Doctor identifier |

#### Query Parameters

Uses standard pagination (`page`, `size`, `sort`).

#### Response

- **Status Code:** `200 OK`
- **Body:** ApiResponse with data list and pagination meta

#### Example Response

```json
{
  "status": 200,
  "success": true,
  "message": "Appointments fetched successfully",
  "data": [
    {
      "appointment_id": "550e8400-e29b-41d4-a716-446655440010",
      "patient_id": "550e8400-e29b-41d4-a716-446655440000",
      "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
      "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
      "start_time": "2025-12-01T09:00:00Z",
      "end_time": "2025-12-01T09:30:00Z",
      "status": "PENDING"
    },
    {
      "appointment_id": "550e8400-e29b-41d4-a716-446655440011",
      "patient_id": "550e8400-e29b-41d4-a716-446655440003",
      "doctor_id": "550e8400-e29b-41d4-a716-446655440001",
      "clinic_id": "550e8400-e29b-41d4-a716-446655440002",
      "start_time": "2025-12-01T14:00:00Z",
      "end_time": "2025-12-01T14:30:00Z",
      "status": "CONFIRMED"
    }
  ],
  "meta": {
    "currentPage": 0,
    "pageSize": 20,
    "totalPages": 1,
    "totalItems": 2
  }
}
```

---

## Data Models

### AppointmentResponseDto

```json
{
  "appointment_id": "UUID",
  "patient_id": "UUID",
  "doctor_id": "UUID",
  "clinic_id": "UUID",
  "start_time": "Date-Time (ISO 8601)",
  "end_time": "Date-Time (ISO 8601)",
  "status": "PENDING | CONFIRMED | CANCELLED | COMPLETED"
}
```

### AppointmentCreateRequest

```json
{
  "patient_id": "UUID (required)",
  "doctor_id": "UUID (optional)",
  "clinic_id": "UUID (required)",
  "start_time": "Date-Time (required, future, ISO 8601)",
  "end_time": "Date-Time (required, future, ISO 8601)"
}
```

### AppointmentUpdateRequest

```json
{
  "doctor_id": "UUID (optional)",
  "clinic_id": "UUID (optional)",
  "start_time": "Date-Time (optional, future, ISO 8601)",
  "end_time": "Date-Time (optional, future, ISO 8601)"
}
```

### RescheduleRequest

```json
{
  "doctor_id": "UUID (optional)",
  "start_time": "Date-Time (required, future, ISO 8601)",
  "end_time": "Date-Time (required, future, ISO 8601)"
}
```

### StatusUpdateRequest

```json
{
  "status": "PENDING | CONFIRMED | CANCELLED | COMPLETED (required)"
}
```

### ApiResponse Envelope

```json
{
  "status": 200,
  "success": true,
  "message": "string",
  "data": {},
  "meta": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPages": 1,
    "totalItems": 1
  }
}
```

### ErrorResponse Envelope

```json
{
  "status": 400,
  "success": false,
  "error": "string",
  "message": "string"
}
```

---

## Validation Rules

### Date-Time Format

- ISO 8601 format: `YYYY-MM-DDTHH:mm:ss[.SSS]Z`
- `start_time` and `end_time` must be in the future for create/reschedule/update.

### Status Values

- `PENDING`
- `CONFIRMED`
- `CANCELLED`
- `COMPLETED`

---

## Notes

- Pagination follows Spring Data conventions using `page`, `size`, and `sort` query params.
- All time fields are in UTC ISO 8601.
- All list/search endpoints use the same underlying `search` method for consistency.
- Time slot conflicts are only checked against `CONFIRMED` appointments.
- The service validates that `start_time` is before `end_time` and in the future.
- All endpoints return consistent `ApiResponse` envelope format.

## Service Architecture

### Design Decisions

1. **Unified Search Method**: All list endpoints (`GET /api/appointments`, `/search`, `/patients/{id}/appointments`, `/doctors/{id}/appointments`) use the same `search` method with different parameter combinations for consistency and maintainability.

2. **Conflict Detection**: Only `CONFIRMED` appointments are considered for time slot conflict detection. `PENDING`, `CANCELLED`, and `COMPLETED` appointments don't block new appointments.

3. **Validation Strategy**:

   - Time validation ensures `start_time < end_time` and `start_time` is in the future
   - Conflict validation prevents double-booking of confirmed appointments
   - All validation errors return descriptive error messages

4. **Response Format**: All endpoints use a consistent `ApiResponse<T>` envelope with status, success flag, message, data, and optional pagination metadata.
