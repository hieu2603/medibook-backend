package com.sgu.appointment_service.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sgu.appointment_service.dto.request.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.request.AppointmentUpdateRequest;
import com.sgu.appointment_service.dto.request.RescheduleRequest;
import com.sgu.appointment_service.dto.request.StatusUpdateRequest;
import com.sgu.appointment_service.dto.response.AppointmentResponseDto;
import com.sgu.appointment_service.dto.response.common.ApiResponse;
import com.sgu.appointment_service.enums.AppointmentStatus;
import com.sgu.appointment_service.service.AppointmentService;
import com.sgu.appointment_service.util.PaginationMetaUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

        private final AppointmentService appointmentService;

        @PostMapping
        public ResponseEntity<ApiResponse<AppointmentResponseDto>> create(
                        @Valid @RequestBody AppointmentCreateRequest request) {
                AppointmentResponseDto created = appointmentService.createAppointment(request);
                ApiResponse<AppointmentResponseDto> body = ApiResponse.<AppointmentResponseDto>builder()
                                .status(HttpStatus.CREATED.value())
                                .success(true)
                                .message("Appointment created successfully")
                                .data(created)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.CREATED);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<AppointmentResponseDto>> getById(@PathVariable UUID id) {
                AppointmentResponseDto dto = appointmentService.getById(id);
                ApiResponse<AppointmentResponseDto> body = ApiResponse.<AppointmentResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointment fetched successfully")
                                .data(dto)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAllAppointments(
                        @RequestParam(required = false) UUID patient_id,
                        @RequestParam(required = false) UUID doctor_id,
                        @RequestParam(required = false) UUID clinic_id,
                        @RequestParam(required = false) AppointmentStatus status,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_from,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_to,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_from,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_to,
                        Pageable pageable) {
                Page<AppointmentResponseDto> page = appointmentService.search(patient_id, doctor_id, clinic_id, status,
                                start_from, start_to, end_from, end_to, pageable);
                ApiResponse<List<AppointmentResponseDto>> body = ApiResponse.<List<AppointmentResponseDto>>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointments fetched successfully")
                                .data(page.getContent())
                                .meta(PaginationMetaUtils.from(page))
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @GetMapping("/search")
        public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> searchAppointments(
                        @RequestParam(required = false) UUID patient_id,
                        @RequestParam(required = false) UUID doctor_id,
                        @RequestParam(required = false) UUID clinic_id,
                        @RequestParam(required = false) AppointmentStatus status,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_from,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_to,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_from,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_to,
                        Pageable pageable) {
                Page<AppointmentResponseDto> page = appointmentService.search(patient_id, doctor_id, clinic_id, status,
                                start_from, start_to, end_from, end_to, pageable);
                ApiResponse<List<AppointmentResponseDto>> body = ApiResponse.<List<AppointmentResponseDto>>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointments fetched successfully")
                                .data(page.getContent())
                                .meta(PaginationMetaUtils.from(page))
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @GetMapping("/availability/doctor")
        public ResponseEntity<ApiResponse<Boolean>> checkAvailability(
                        @RequestParam UUID clinic_id,
                        @RequestParam(required = false) UUID doctor_id,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_time,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_time) {
                boolean available = appointmentService.isAvailable(clinic_id, doctor_id, start_time, end_time);
                ApiResponse<Boolean> body = ApiResponse.<Boolean>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Availability checked successfully")
                                .data(available)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<AppointmentResponseDto>> updateAppointment(@PathVariable UUID id,
                        @Valid @RequestBody AppointmentUpdateRequest request) {
                AppointmentResponseDto updated = appointmentService.updateAppointment(id, request);
                ApiResponse<AppointmentResponseDto> body = ApiResponse.<AppointmentResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointment updated successfully")
                                .data(updated)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @PostMapping("/{id}/reschedule")
        public ResponseEntity<ApiResponse<AppointmentResponseDto>> rescheduleAppointment(@PathVariable UUID id,
                        @Valid @RequestBody RescheduleRequest request) {
                AppointmentResponseDto updated = appointmentService.reschedule(id, request);
                ApiResponse<AppointmentResponseDto> body = ApiResponse.<AppointmentResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointment rescheduled successfully")
                                .data(updated)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @PatchMapping("/{id}/status")
        public ResponseEntity<ApiResponse<AppointmentResponseDto>> updateAppointmentStatus(@PathVariable UUID id,
                        @Valid @RequestBody StatusUpdateRequest request) {
                AppointmentResponseDto updated = appointmentService.updateStatus(id, request.getStatus());
                ApiResponse<AppointmentResponseDto> body = ApiResponse.<AppointmentResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointment status updated successfully")
                                .data(updated)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @PostMapping("/{id}/confirm")
        public ResponseEntity<ApiResponse<AppointmentResponseDto>> confirmAppointment(@PathVariable UUID id) {
                AppointmentResponseDto updated = appointmentService.updateStatus(id, AppointmentStatus.CONFIRMED);
                ApiResponse<AppointmentResponseDto> body = ApiResponse.<AppointmentResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointment confirmed successfully")
                                .data(updated)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @PostMapping("/{id}/cancel")
        public ResponseEntity<ApiResponse<AppointmentResponseDto>> cancelAppointment(@PathVariable UUID id) {
                AppointmentResponseDto updated = appointmentService.updateStatus(id, AppointmentStatus.CANCELLED);
                ApiResponse<AppointmentResponseDto> body = ApiResponse.<AppointmentResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointment cancelled successfully")
                                .data(updated)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @PostMapping("/{id}/complete")
        public ResponseEntity<ApiResponse<AppointmentResponseDto>> completeAppointment(@PathVariable UUID id) {
                AppointmentResponseDto updated = appointmentService.updateStatus(id, AppointmentStatus.COMPLETED);
                ApiResponse<AppointmentResponseDto> body = ApiResponse.<AppointmentResponseDto>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointment completed successfully")
                                .data(updated)
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteAppointment(@PathVariable UUID id) {
                appointmentService.deleteAppointment(id);
                ApiResponse<Void> body = ApiResponse.<Void>builder()
                                .status(HttpStatus.NO_CONTENT.value())
                                .success(true)
                                .message("Appointment deleted successfully")
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @GetMapping("/patients/{patientId}/appointments")
        public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> listAppointmentsByPatient(
                        @PathVariable UUID patientId,
                        Pageable pageable) {
                Page<AppointmentResponseDto> page = appointmentService.search(patientId, null, null, null, null, null,
                                null,
                                null, pageable);
                ApiResponse<List<AppointmentResponseDto>> body = ApiResponse.<List<AppointmentResponseDto>>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointments fetched successfully")
                                .data(page.getContent())
                                .meta(PaginationMetaUtils.from(page))
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }

        @GetMapping("/doctors/{doctorId}/appointments")
        public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> listAppointmentsByDoctor(
                        @PathVariable UUID doctorId,
                        Pageable pageable) {
                Page<AppointmentResponseDto> page = appointmentService.search(null, doctorId, null, null, null, null,
                                null,
                                null, pageable);
                ApiResponse<List<AppointmentResponseDto>> body = ApiResponse.<List<AppointmentResponseDto>>builder()
                                .status(HttpStatus.OK.value())
                                .success(true)
                                .message("Appointments fetched successfully")
                                .data(page.getContent())
                                .meta(PaginationMetaUtils.from(page))
                                .build();
                return new ResponseEntity<>(body, HttpStatus.OK);
        }
}
