package com.company.webservice.payloads;

import lombok.Value;
import org.springframework.data.domain.Page;

@Value
public class PaginationMeta {

    private final long totalElements;
    private final int totalPages;

    public static <T> PaginationMeta of(Page<T> page) {
        return new PaginationMeta(page.getTotalElements(), page.getTotalPages());
    }
}
