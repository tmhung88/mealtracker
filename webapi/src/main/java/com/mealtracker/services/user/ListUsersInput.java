package com.mealtracker.services.user;

import com.mealtracker.services.pagination.PageableOrder;
import com.mealtracker.services.pagination.SingleSortableColumnPageableParams;
import com.mealtracker.validation.ValidString;
import lombok.Data;

@Data
public class ListUsersInput extends SingleSortableColumnPageableParams {
    @ValidString(values = {"id", "email", "fullName"})
    private String orderBy = "id";

    @ValidString(values = {"asc", "desc"})
    private String order = "desc";

    public PageableOrder getOrder() {
        return PageableOrder.valueOf(order.toUpperCase());
    }
}
