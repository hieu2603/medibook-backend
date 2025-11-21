package com.sgu.appointment_service.mapper;

import com.sgu.appointment_service.dto.request.AppointmentCreateRequest;
import com.sgu.appointment_service.dto.response.appointment.AppointmentResponseDto;
import com.sgu.appointment_service.model.Appointment;

public class AppointmentMapper {

    // Từ Create DTO -> Entity
    public static Appointment toEntity(AppointmentCreateRequest dto) {
        return Appointment.builder()
                .patientId(dto.getPatientId())
                .doctorId(dto.getDoctorId())
                .clinicId(dto.getClinicId())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .build();
    }

    // Từ Entity -> Response DTO
    public static AppointmentResponseDto toDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .clinicId(appointment.getClinicId())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .price(appointment.getPrice())
                .description(appointment.getDescription())
                .status(appointment.getStatus())
                .build();
    }
}
