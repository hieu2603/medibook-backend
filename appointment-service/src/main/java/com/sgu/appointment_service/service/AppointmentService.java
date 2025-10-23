package com.sgu.appointment_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sgu.appointment_service.dto.request.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.request.AppointmentUpdateRequest;
import com.sgu.appointment_service.dto.request.RescheduleRequest;
import com.sgu.appointment_service.dto.response.AppointmentResponseDto;
import com.sgu.appointment_service.enums.AppointmentStatus;

public interface AppointmentService {

    AppointmentResponseDto createAppointment(AppointmentCreateRequest request);

    AppointmentResponseDto getById(UUID appointmentId);

    Page<AppointmentResponseDto> search(UUID patientId, UUID doctorId, UUID clinicId, AppointmentStatus status,
            LocalDateTime startFrom, LocalDateTime startTo, LocalDateTime endFrom, LocalDateTime endTo,
            Pageable pageable);

    AppointmentResponseDto updateAppointment(UUID appointmentId, AppointmentUpdateRequest request);

    void deleteAppointment(UUID appointmentId);

    AppointmentResponseDto updateStatus(UUID appointmentId, AppointmentStatus status);

    AppointmentResponseDto reschedule(UUID appointmentId, RescheduleRequest request);

    boolean isAvailable(UUID clinicId, UUID doctorId, LocalDateTime startTime, LocalDateTime endTime);
}
