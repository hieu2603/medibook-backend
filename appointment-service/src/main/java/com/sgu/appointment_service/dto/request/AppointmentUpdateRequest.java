package com.sgu.appointment_service.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
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
    
    @DecimalMin(value = "0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;
    
    private String description;
}
