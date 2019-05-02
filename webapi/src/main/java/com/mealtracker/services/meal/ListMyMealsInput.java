package com.mealtracker.services.meal;

import com.mealtracker.services.pagination.PageableOrder;
import com.mealtracker.services.pagination.PageableParams;
import com.mealtracker.services.pagination.SortableProperty;
import com.mealtracker.validation.LocalDateFormat;
import com.mealtracker.validation.LocalTimeFormat;
import com.mealtracker.validation.ValidString;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ListMyMealsInput implements PageableParams {

    @NotNull
    @LocalDateFormat
    private String fromDate;

    @NotNull
    @LocalDateFormat
    private String toDate;

    @NotNull
    @LocalTimeFormat
    private String fromTime;

    @NotNull
    @LocalTimeFormat
    private String toTime;

    @ValidString(values = {"name", "consumedDate", "consumedTime", "calories"})
    private String orderBy = "consumedDate";

    @ValidString(values = {"asc", "desc"})
    private String order = "desc";

    private int rowsPerPage = 10;
    private int pageIndex = 0;


    @Override
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public List<SortableProperty> getSortableProperties() {
        return SortableProperty.singlePropertyOrder(orderBy, getOrder());
    }

    public LocalDate getFromDate() {
        return LocalDate.parse(fromDate);
    }

    public LocalDate getToDate() {
        return LocalDate.parse(toDate);
    }

    public LocalTime getFromTime() {
        return LocalTime.parse(fromTime);
    }

    public LocalTime getToTime() {
        return LocalTime.parse(toTime);
    }

    public PageableOrder getOrder() {
        return PageableOrder.valueOf(order.toUpperCase());
    }
}
