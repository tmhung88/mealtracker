package com.company.webservice.services.pagination;

import com.company.webservice.validation.ValueInList;
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

    @ValueInList({"asc", "desc"})
    private String order = "desc";

    public abstract String getOrderBy();

    @Override
    public List<SortableProperty> getSortableProperties() {
        return SortableProperty.singlePropertyOrder(getOrderBy(), PageableOrder.valueOf(order.toUpperCase()));
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
