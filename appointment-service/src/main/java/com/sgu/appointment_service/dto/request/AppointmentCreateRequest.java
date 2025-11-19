package com.sgu.appointment_service.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateRequest {
    @NotNull
    private UUID patient_id;

    private UUID doctor_id;

    @NotNull
    private UUID clinic_id;

    @NotNull
    @Future
    private LocalDateTime start_time;

    @NotNull
    @Future
    private LocalDateTime end_time;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    private String description;
}
