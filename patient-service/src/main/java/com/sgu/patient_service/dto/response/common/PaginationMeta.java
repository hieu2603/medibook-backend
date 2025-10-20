package com.sgu.patient_service.dto.response.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationMeta {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
}
