package com.sgu.appointment_service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sgu.appointment_service.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {
    private UUID appointment_id;
    private UUID patient_id;
    private UUID doctor_id;
    private UUID clinic_id;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private AppointmentStatus status;
}
