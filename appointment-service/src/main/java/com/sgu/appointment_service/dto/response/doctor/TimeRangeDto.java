package com.sgu.appointment_service.dto.response.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeRangeDto {
    private LocalTime startTime;
    private LocalTime endTime;
}
