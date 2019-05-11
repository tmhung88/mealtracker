package com.mealtracker.services.meal;

import com.mealtracker.services.pagination.PageableOrder;
import com.mealtracker.services.pagination.PageableParams;
import com.mealtracker.services.pagination.SortableProperty;
import com.mealtracker.validation.ValueInList;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
public class ListMealsInput implements PageableParams {

    @ValueInList(value = {"name", "consumedDate", "consumedTime", "calories"})
    private String orderBy = "consumedDate";

    @ValueInList(value = {"asc", "desc"})
    private String order = "desc";

    @Min(1)
    @Max(50)
    private int rowsPerPage = 10;

    @PositiveOrZero
    private int pageIndex = 0;

    @Override
    public List<SortableProperty> getSortableProperties() {
        return SortableProperty.singlePropertyOrder(orderBy, getOrder());
    }

    public PageableOrder getOrder() {
        return PageableOrder.valueOf(order.toUpperCase());
    }
}
