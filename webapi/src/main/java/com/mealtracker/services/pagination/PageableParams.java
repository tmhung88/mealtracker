package com.mealtracker.services.pagination;

import java.util.List;

public interface PageableParams {

    int getRowsPerPage();

    int getPageIndex();

    List<SortableProperty> getSortableProperties();
}
