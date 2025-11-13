package com.sgu.auth_service.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int status;

    @Builder.Default
    private boolean success = true;

    private String message;

    private T data;
    private PaginationMeta meta;
}
