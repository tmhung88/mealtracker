package com.mealtracker.services.meal;

import com.mealtracker.services.pagination.PageableOrder;
import com.mealtracker.services.pagination.PageableParams;
import com.mealtracker.services.pagination.SortableProperty;
import com.mealtracker.validation.LocalDateFormat;
import com.mealtracker.validation.LocalTimeFormat;
import com.mealtracker.validation.ValidString;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ListMyMealsInput implements PageableParams {

    @LocalDateFormat
    private String fromDate;

    @LocalDateFormat
    private String toDate;

    @LocalTimeFormat
    private String fromTime;

    @LocalTimeFormat
    private String toTime;

    @ValidString(values = {"name", "consumedDate", "consumedTime", "calories"})
    private String orderBy = "consumedDate";

    @ValidString(values = {"asc", "desc"})
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

    public LocalDate getFromDate() {
        return fromDate == null? null : LocalDate.parse(fromDate);
    }

    public LocalDate getToDate() {
        return toDate == null? null : LocalDate.parse(toDate);
    }

    public LocalTime getFromTime() {
        return fromTime == null ? null : LocalTime.parse(fromTime);
    }

    public LocalTime getToTime() {
        return toTime == null ? null : LocalTime.parse(toTime);
    }

    public PageableOrder getOrder() {
        return PageableOrder.valueOf(order.toUpperCase());
    }
}
