package com.sgu.appointment_service.dto.request.appointment;

import jakarta.validation.constraints.Future;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AppointmentUpdateRequest {
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    private String description;

    private UUID doctorId;
}
