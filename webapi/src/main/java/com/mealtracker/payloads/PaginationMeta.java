package com.mealtracker.payloads;

import lombok.Value;
import org.springframework.data.domain.Slice;

@Value
public class PaginationMeta {
    private final boolean hasNext;
    private final boolean hasPrevious;
    public static <T> PaginationMeta of(Slice<T> slice) {
        return new PaginationMeta(slice.hasNext(), slice.hasPrevious());
    }
}
