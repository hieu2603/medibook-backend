package com.sgu.appointment_service.service;

import com.sgu.appointment_service.constant.AppointmentStatus;
import com.sgu.appointment_service.dto.request.appointment.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.request.appointment.AppointmentUpdateRequest;
import com.sgu.appointment_service.dto.response.appointment.AppointmentResponseDto;
import com.sgu.appointment_service.dto.response.common.PaginationResponse;
import com.sgu.appointment_service.dto.response.doctor.DoctorAvailableResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AppointmentService {
    PaginationResponse<AppointmentResponseDto> getAppointments(
            UUID patientId,
            UUID clinicId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            AppointmentStatus status,
            int page, int size
    );

    AppointmentResponseDto createAppointment(AppointmentCreateRequest dto);

    void confirmAppointment(UUID appointmentId);

    AppointmentResponseDto getAppointmentById(UUID appointmentId);

    AppointmentResponseDto updateAppointment(UUID appointmentId, AppointmentUpdateRequest dto);

    void cancelAppointment(UUID appointmentId);

    void completeAppointment(UUID appointmentId);

    DoctorAvailableResponse getDoctorAvailableSlots(UUID doctorId, LocalDate date);
}
