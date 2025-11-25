package com.sgu.appointment_service.controller;

import com.sgu.appointment_service.constant.AppointmentStatus;
import com.sgu.appointment_service.dto.request.appointment.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.request.appointment.AppointmentUpdateRequest;
import com.sgu.appointment_service.dto.response.appointment.AppointmentResponseDto;
import com.sgu.appointment_service.dto.response.common.ApiResponse;
import com.sgu.appointment_service.dto.response.common.PaginationResponse;
import com.sgu.appointment_service.dto.response.doctor.DoctorAvailableResponse;
import com.sgu.appointment_service.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAppointments(
            @RequestParam(required = false) UUID patientId,
            @RequestParam(required = false) UUID clinicId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginationResponse<AppointmentResponseDto> result = appointmentService
                .getAppointments(patientId, clinicId, startTime, endTime, status, page, size);

        ApiResponse<List<AppointmentResponseDto>> response = ApiResponse.<List<AppointmentResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Appointments retrieved successfully")
                .data(result.getData())
                .meta(result.getMeta())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> createAppointment(
            @Valid @RequestBody AppointmentCreateRequest dto
    ) {
        AppointmentResponseDto createdAppointment = appointmentService
                .createAppointment(dto);

        ApiResponse<AppointmentResponseDto> response = ApiResponse.<AppointmentResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Appointment created successfully")
                .data(createdAppointment)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{appointmentId}/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmAppointment(
            @PathVariable UUID appointmentId
    ) {
        appointmentService.confirmAppointment(appointmentId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Appointment %s confirmed successfully".formatted(appointmentId))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> getAppointmentById(
            @PathVariable UUID appointmentId
    ) {
        AppointmentResponseDto appointment = appointmentService
                .getAppointmentById(appointmentId);

        ApiResponse<AppointmentResponseDto> response = ApiResponse.<AppointmentResponseDto>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Appointment %s retrieved successfully".formatted(appointmentId))
                .data(appointment)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponseDto>> updateAppointment(
            @PathVariable UUID appointmentId,
            @Valid @RequestBody AppointmentUpdateRequest dto
    ) {
        AppointmentResponseDto updatedAppointment = appointmentService
                .updateAppointment(appointmentId, dto);

        ApiResponse<AppointmentResponseDto> response = ApiResponse.<AppointmentResponseDto>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Appointment %s updated successfully".formatted(appointmentId))
                .data(updatedAppointment)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(
            @PathVariable UUID appointmentId
    ) {
        appointmentService.cancelAppointment(appointmentId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Appointment %s cancelled successfully".formatted(appointmentId))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/{appointmentId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeAppointment(
            @PathVariable UUID appointmentId
    ) {
        appointmentService.completeAppointment(appointmentId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Appointment %s confirmed successfully".formatted(appointmentId))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/available-slots")
    public ResponseEntity<ApiResponse<DoctorAvailableResponse>> getDoctorAvailableSlots(
            @RequestParam UUID doctorId,
            @RequestParam LocalDate date
    ) {
        DoctorAvailableResponse doctorAvailableSlots = appointmentService
                .getDoctorAvailableSlots(doctorId, date);

        ApiResponse<DoctorAvailableResponse> response = ApiResponse.<DoctorAvailableResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Fetched available slots of doctor %s successfully".formatted(doctorId))
                .data(doctorAvailableSlots)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
