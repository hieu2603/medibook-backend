package com.sgu.appointment_service.dto.response.appointment;

import com.sgu.appointment_service.constant.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AppointmentResponseDto {
    private UUID appointmentId;
    private UUID patientId;
    private UUID doctorId;
    private UUID clinicId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private String description;
    private AppointmentStatus status;
}
