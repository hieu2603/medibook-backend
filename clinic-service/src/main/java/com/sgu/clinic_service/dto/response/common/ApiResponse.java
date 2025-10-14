package com.sgu.clinic_service.dto.response.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private int status;
    private boolean success;
    private String message;
    private T data;
}
