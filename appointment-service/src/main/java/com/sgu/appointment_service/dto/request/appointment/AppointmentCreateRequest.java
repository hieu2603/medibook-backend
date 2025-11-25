package com.sgu.appointment_service.dto.request.appointment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AppointmentCreateRequest {
    @NotNull(message = "User ID of patient is required")
    private UUID patientId; // Truyền user id của patient

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "User ID of clinic is required")
    private UUID clinicId; // Truyền user id của clinic

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    private String description;
}
