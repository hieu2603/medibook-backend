package com.sgu.patient_service.dto.response.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private int status;
    private boolean success;
    private String error;
    private String message;
}
