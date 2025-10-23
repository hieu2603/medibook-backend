package com.sgu.appointment_service.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentUpdateRequest {
    private UUID doctor_id;
    private UUID clinic_id;
    @Future
    private LocalDateTime start_time;
    @Future
    private LocalDateTime end_time;
}
