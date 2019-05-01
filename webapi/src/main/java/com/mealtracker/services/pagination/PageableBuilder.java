package com.mealtracker.services.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PageableBuilder {

    public Pageable build(PageableParams pageableParams) {
        var sortableColumns = new ArrayList<>(pageableParams.getSortableProperties());
        sortableColumns.sort(SortableProperty.BY_PRIORITY);
        var orders = sortableColumns.stream().map(SortableProperty::toSpringOrder).collect(Collectors.toList());
        return PageRequest.of(pageableParams.getPageIndex(), pageableParams.getRowsPerPage(), Sort.by(orders));
    }
}
