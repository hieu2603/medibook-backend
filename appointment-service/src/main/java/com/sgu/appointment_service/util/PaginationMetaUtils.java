package com.sgu.appointment_service.util;

import com.sgu.appointment_service.dto.response.common.PaginationMeta;
import org.springframework.data.domain.Page;

public final class PaginationMetaUtils {
    private PaginationMetaUtils() {
    }

    public static PaginationMeta from(Page<?> page) {
        return PaginationMeta.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .build();
    }
}
