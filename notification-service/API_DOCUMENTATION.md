Notification Service API Documentation
Base URL
http://localhost:8089/api/notifications

Content Type

All requests and responses use application/json.

Error Response Format
{
  "status": 400,
  "success": false,
  "error": "Bad Request",
  "message": "Validation failed"
}

Endpoints
1. Create Notification

POST /create

Tạo một thông báo mới. Nếu message không gửi, backend sẽ tự lấy mặc định từ NotificationType.

Request Body
Field	Type	Required	Description
userId	UUID	Yes	ID của user nhận thông báo
title	String	No	Tiêu đề thông báo. Mặc định: "Thông báo hệ thống"
type	Enum	Yes	Loại thông báo, giá trị: "APPOINTMENT_SUCCESS" hoặc "APPOINTMENT_CANCELED"
message	String	No	Nội dung thông báo. Nếu không gửi, backend sẽ dùng mặc định từ enum
Example Request
{
  "userId": "123e4567-e89b-12d3-a456-426614174012",
  "title": "Thông báo hệ thống",
  "type": "APPOINTMENT_CANCELED"
}

Example Response
{
  "status": 201,
  "success": true,
  "message": "Notification created successfully",
  "data": {
    "notificationId": "41ff4f91-fe6a-4b8b-8ae1-f6b1fe94f8c9",
    "userId": "123e4567-e89b-12d3-a456-426614174012",
    "title": "Thông báo hệ thống",
    "message": "Đặt lịch khám đã bị hủy",
    "type": "APPOINTMENT_CANCELED",
    "isRead": false,
    "createdAt": "2025-10-28T14:00:00.000Z"
  }
}

2. Get All Notifications of a User

GET /user/{userId}

Path Parameters
Parameter	Type	Required	Description
userId	UUID	Yes	ID của user
Example Response
{
  "status": 200,
  "success": true,
  "message": "User notifications fetched successfully",
  "data": [
    {
      "notificationId": "41ff4f91-fe6a-4b8b-8ae1-f6b1fe94f8c9",
      "userId": "123e4567-e89b-12d3-a456-426614174012",
      "title": "Thông báo hệ thống",
      "message": "Đặt lịch khám đã bị hủy",
      "type": "APPOINTMENT_CANCELED",
      "isRead": false,
      "createdAt": "2025-10-28T14:00:00.000Z"
    }
  ]
}

3. Delete Notification by ID

DELETE /{notificationId}

Example Response
{
  "status": 204,
  "success": true,
  "message": "Notification deleted successfully"
}

4. Mark Notification as Read

PUT /{notificationId}/mark-read

Example Response
{
  "status": 200,
  "success": true,
  "message": "Notification marked as read",
  "data": {
    "notificationId": "41ff4f91-fe6a-4b8b-8ae1-f6b1fe94f8c9",
    "isRead": true
  }
}

5. Mark All Notifications of a User as Read

PUT /user/{userId}/mark-all-read

Example Response
{
  "status": 200,
  "success": true,
  "message": "All notifications marked as read for user 123e4567-e89b-12d3-a456-426614174012"
}

6. Delete All Notifications of a User

DELETE /user/{userId}/delete-all

Example Response
{
  "status": 204,
  "success": true,
  "message": "All notifications deleted for user 123e4567-e89b-12d3-a456-426614174012"
}

NotificationType Enum
Value	Default Message
APPOINTMENT_SUCCESS	Đặt lịch khám thành công
APPOINTMENT_CANCELED	Đặt lịch khám đã bị hủy