(The file `c:\THUYTRANG\J2EE\medibook-backend\payment-service\API_DOCUMENTATION.md` exists, but is empty)
```markdown
# Payment Service API Documentation

## Base URL

```shell
http://localhost:8083/api/payments
```

## Content Type

All requests and responses use `application/json` unless noted otherwise (VNPay callback returns HTML).

## Error Responses

All errors follow a common envelope similar to other services:

```json
{
	"status": 400,
	"success": false,
	"error": "Bad Request",
	"message": "Validation failed"
}
```

Examples:

- Validation Error (400)

```json
{
	"status": 400,
	"success": false,
	"error": "Validation Failed",
	"message": "{amount=Minimum is 10000}"
}
```

- Not Found (404)

```json
{
	"status": 404,
	"success": false,
	"error": "Not Found",
	"message": "Payment not found"
}
```

---

## Endpoints

### 1. Create Deposit (VNPay)

**POST** `/api/payments/deposit`

Creates a VNPay deposit payment and returns a redirect URL. The client can optionally request a pre-selected bank using `bankCode` (e.g. `NCB` for sandbox tests).

#### Request Body

| Field       | Type     | Required | Validation/Notes |
| ----------- | -------- | -------- | ---------------- |
| `userId`    | String   | Yes      | UUID/string user id |
| `amount`    | BigDecimal | Yes    | Minimum 10000 (VND) — amount is in VND |
| `description` | String | No       | Optional description |
| `bankCode`  | String   | No       | Optional: VNPay bank code (sandbox may not support all codes) |
| `language`  | String   | No       | Optional, default `vn` |

#### Example Request Body

```json
{
	"userId": "550e8400-e29b-41d4-a716-446655440000",
	"amount": 100000,
	"description": "Nạp tiền vào tài khoản",
}
```

#### Response

- **Status Code:** `200 OK`
- **Body:** `VNPayPaymentResponse`

#### Example Response

```json
{
	"code": "00",
	"message": "Success",
	"paymentUrl": "https://sandbox.vnpayment.vn/payment_url?....&vnp_SecureHash=abcd1234"
}
```
# Ngân hàng	NCB
# Số thẻ	9704198526191432198
# Tên chủ thẻ	NGUYEN VAN A
# Ngày phát hành	07/15
# Mật khẩu OTP	123456

---

### 2. Withdraw

**POST** `/api/payments/withdraw`

Initiate a withdraw request. This creates a withdrawal record and (in future) triggers bank transfer processing.

#### Request Body (WithdrawRequest)

| Field       | Type      | Required | Description |
| ----------- | --------- | -------- | ----------- |
| `userId`    | String    | Yes      | User performing withdraw |
| `amount`    | BigDecimal| Yes      | Amount to withdraw in VND |
| `bankAccount` | String  | Yes      | Destination bank account number |
| `bankName`  | String    | Yes      | Destination bank name |
| `description` | String  | No       | Optional note |

#### Response

- **Status Code:** `201 Created`
- **Body:** `PaymentResponse` (status will usually be `PENDING`)

Example (simplified):

```json
{
	"paymentId": "...",
	"userId": "...",
	"amount": 500000,
	"paymentType": "WITHDRAW",
	"status": "PENDING",
	"bankName": "ACB",
	"bankAccount": "****1234",
	"createdAt": "2025-11-06T14:00:00"
}
```

---

### 3. VNPay Callback (User Redirect)

**GET** `/api/payments/vnpay-callback`

This is the return URL VNPay redirects the user to after payment. VNPay will append query parameters including `vnp_SecureHash`. The service verifies the signature, updates payment status and returns a simple HTML result page (success or failure).

Notes:
- Response is `text/html` intended to be shown to the user.
- The server validates `vnp_SecureHash` using HMAC-SHA512 and configured secret. If invalid, payment is rejected.

---

### 4. VNPay IPN (Server-to-Server)

**POST** `/api/payments/vnpay-ipn`

Instant Payment Notification endpoint used by VNPay to notify result server-to-server. Accepts the same VNPay parameters as the callback.

#### Response

- On success: `200 OK` with body `{ "RspCode":"00", "Message":"success" }`
- On failure: `200 OK` with `{ "RspCode":"99", "Message":"fail" }`

---

### 5. Get Payment History

**GET** `/api/payments/history/{userId}`

Retrieve the payment history for a user (both deposits and withdraws).

#### Path Parameters

| Parameter | Type   | Required | Description |
| --------- | ------ | -------- | ----------- |
| `userId`  | String | Yes      | User identifier |

#### Response

- **Status Code:** `200 OK`
- **Body:** List of `PaymentResponse` objects

---

### 6. Get Payment by ID

**GET** `/api/payments/{paymentId}`

Retrieve a single payment detail by `paymentId`.

#### Response

- **Status Code:** `200 OK`
- **Body:** `PaymentResponse`

---

## Data Models

### VNPayPaymentRequest

```json
{
	"userId": "String",
	"amount": "BigDecimal (VND)",
	"description": "String (optional)",
	"bankCode": "String (optional, VNPay bank code)",
	"language": "String (optional, e.g. vn)"
}
```

### VNPayPaymentResponse

```json
{
	"code": "00",
	"message": "Success",
	"paymentUrl": "https://sandbox.vnpayment.vn/payment_url?......"
}
```

### WithdrawRequest

```json
{
	"userId": "String",
	"amount": "BigDecimal",
	"bankAccount": "String",
	"bankName": "String",
	"description": "String (optional)"
}
```

### PaymentResponse

```json
{
	"paymentId": "String",
	"userId": "String",
	"amount": "BigDecimal",
	"paymentType": "DEPOSIT | WITHDRAW",
	"status": "PENDING | COMPLETED | FAILED",
	"paymentMethod": "VNPAY | BANK_TRANSFER | ...",
	"bankName": "String (for withdraw)",
	"bankAccount": "String (masked)",
	"transactionId": "String (gateway txn id)",
	"description": "String",
	"createdAt": "ISO 8601",
	"updatedAt": "ISO 8601"
}
```

## VNPay notes & troubleshooting

- Signature: VNPay uses HMAC-SHA512 over a canonical query string. The service computes signature using the configured secret and compares to `vnp_SecureHash`. If you get "Sai chữ ký" (invalid signature):
	- Ensure `vnp_SecureHash` and `vnp_SecureHashType` are removed before recomputing the canonical string.
	- Ensure encoding is UTF-8 and spaces are encoded the same way VNPay expects (implementation uses `URLEncoder.encode` / `quote_plus` semantics).
	- Trim the secret key and make sure it matches the one in VNPay merchant settings.

- BankCode: only pass `vnp_BankCode` (via `bankCode` in request) if you want to pre-select bank. Sandbox may not support all bank codes — try `NCB` for sandbox.

## How to test locally

1. Start the service:

```powershell
cd C:\THUYTRANG\J2EE\medibook-backend\payment-service
.\mvnw spring-boot:run
```

2. Create a deposit (example with PowerShell `curl`):

```powershell
curl -Method POST -Uri http://localhost:8083/api/payments/deposit -ContentType application/json -Body '{"userId":"user-1","amount":100000,"bankCode":"NCB","language":"vn"}'
```

3. Use the returned `paymentUrl` to open VNPay sandbox page (or copy it to browser).

4. When VNPay redirects back, check application logs for signature verification and status updates.

---

If you want, I can add a small dev-only debug endpoint that returns the canonical string and computed HMAC for a given `vnp_Params` so you can compare with VNPay's callback. Reply `yes` and I'll add it along with a short test example.

```
