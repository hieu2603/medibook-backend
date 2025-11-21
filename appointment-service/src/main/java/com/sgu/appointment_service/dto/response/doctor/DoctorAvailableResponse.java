package com.sgu.appointment_service.dto.response.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DoctorAvailableResponse {
    private UUID doctorId;
    private LocalDate date;
    private List<TimeRangeDto> availableRanges;
}
