package com.mealtracker.services.meal;

import com.mealtracker.services.pagination.SingleSortableColumnPageableParams;
import com.mealtracker.validation.ValueInList;
import lombok.Data;

@Data
public class ListMealsInput extends SingleSortableColumnPageableParams {

    @ValueInList(value = {"name", "consumedDate", "consumedTime", "calories"})
    private String orderBy = "consumedDate";

}
