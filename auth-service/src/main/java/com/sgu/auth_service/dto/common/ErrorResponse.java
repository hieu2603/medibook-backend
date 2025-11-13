package com.sgu.auth_service.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private int status;

    @Builder.Default
    private boolean success = false;

    private String error;

    private String message;

    private List<String> details;
}
