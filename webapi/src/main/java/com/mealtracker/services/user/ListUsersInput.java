package com.mealtracker.services.user;

import com.mealtracker.services.pagination.SingleSortableColumnPageableParams;
import com.mealtracker.validation.ValueInList;
import lombok.Data;

@Data
public class ListUsersInput extends SingleSortableColumnPageableParams {
    private static final String CALORIE_REQUEST_VALUE = "dailyCalorieLimit";
    private static final String CALORIE_ENTITY_PATH = "userSettings.dailyCalorieLimit";

    @ValueInList({"id", "email", "fullName", "role", CALORIE_REQUEST_VALUE})
    private String orderBy = "id";

    public String getOrderBy() {
        return CALORIE_REQUEST_VALUE.equals(orderBy) ? CALORIE_ENTITY_PATH : orderBy;
    }
}
