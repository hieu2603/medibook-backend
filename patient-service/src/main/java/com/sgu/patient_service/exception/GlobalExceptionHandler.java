package com.sgu.patient_service.exception;

import com.sgu.patient_service.dto.response.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .success(false)
                .error("Bad Request")
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .success(false)
                .error("Not Found")
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .success(false)
                .error("Validation Failed")
                .message(errors.toString())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}

// package com.sgu.patient_service.exception;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.Map;

// @RestControllerAdvice
// public class GlobalExceptionHandler {

// @ExceptionHandler(PatientNotFoundException.class)
// public ResponseEntity<Map<String, Object>>
// handlePatientNotFoundException(PatientNotFoundException ex) {
// Map<String, Object> error = new HashMap<>();
// error.put("timestamp", LocalDateTime.now());
// error.put("status", HttpStatus.NOT_FOUND.value());
// error.put("error", "Not Found");
// error.put("message", ex.getMessage());
// error.put("path", "/api/patients");
// return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
// }

// @ExceptionHandler(MethodArgumentNotValidException.class)
// public ResponseEntity<Map<String, Object>>
// handleValidationExceptions(MethodArgumentNotValidException ex) {
// Map<String, Object> error = new HashMap<>();
// error.put("timestamp", LocalDateTime.now());
// error.put("status", HttpStatus.BAD_REQUEST.value());
// error.put("error", "Bad Request");
// error.put("message", "Validation failed");

// Map<String, String> fieldErrors = new HashMap<>();
// ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
// fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
// });
// error.put("fieldErrors", fieldErrors);

// return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
// }

// @ExceptionHandler(IllegalArgumentException.class)
// public ResponseEntity<Map<String, Object>>
// handleIllegalArgumentException(IllegalArgumentException ex) {
// Map<String, Object> error = new HashMap<>();
// error.put("timestamp", LocalDateTime.now());
// error.put("status", HttpStatus.BAD_REQUEST.value());
// error.put("error", "Bad Request");
// error.put("message", ex.getMessage());
// return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
// }

// @ExceptionHandler(Exception.class)
// public ResponseEntity<Map<String, Object>> handleGenericException(Exception
// ex) {
// Map<String, Object> error = new HashMap<>();
// error.put("timestamp", LocalDateTime.now());
// error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
// error.put("error", "Internal Server Error");
// error.put("message", "An unexpected error occurred");
// return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }
