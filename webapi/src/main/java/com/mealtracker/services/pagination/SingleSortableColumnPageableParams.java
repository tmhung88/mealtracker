package com.mealtracker.services.pagination;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
public abstract class SingleSortableColumnPageableParams implements PageableParams {

    @Min(1)
    @Max(50)
    private int rowsPerPage = 10;

    @PositiveOrZero
    private int pageIndex = 0;

    public abstract PageableOrder getOrder();

    public abstract String getOrderBy();

    @Override
    public List<SortableProperty> getSortableProperties() {
        return SortableProperty.singlePropertyOrder(getOrderBy(), getOrder());
    }

    @Override
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }
}
