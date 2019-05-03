package com.mealtracker.utils;

import org.mockito.Mockito;
import org.springframework.data.domain.Page;

import java.util.Arrays;

import static org.mockito.Mockito.when;

public class MockPageBuilder {

    public static <T> Page<T> oneRowsPerPage(long totalElements, T... data) {
        var page = Mockito.mock(Page.class);
        when(page.getTotalElements()).thenReturn(totalElements);
        when(page.getTotalPages()).thenReturn((int) totalElements);
        when(page.getContent()).thenReturn(Arrays.asList(data));
        return page;
    }
}
