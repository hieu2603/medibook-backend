package com.sgu.user_service.dto.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationResponse<T> {
    private List<T> data;
    private PaginationMeta meta;
}
