package com.mealtracker.services.meal;

import com.mealtracker.services.pagination.SingleSortableColumnPageableParams;
import com.mealtracker.validation.LocalDateFormat;
import com.mealtracker.validation.LocalTimeFormat;
import com.mealtracker.validation.ValueInList;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Data
public class ListMyMealsInput extends SingleSortableColumnPageableParams {

    @LocalDateFormat
    private String fromDate;

    @LocalDateFormat
    private String toDate;

    @LocalTimeFormat
    private String fromTime;

    @LocalTimeFormat
    private String toTime;

    @ValueInList({"name", "consumedDate", "consumedTime", "calories"})
    private String orderBy = "consumedDate";

    public LocalDate getFromDate() {
        return Optional.ofNullable(fromDate).map(LocalDate::parse).orElse(null);
    }

    public LocalDate getToDate() {
        return Optional.ofNullable(toDate).map(LocalDate::parse).orElse(null);
    }

    public LocalTime getFromTime() {
        return Optional.ofNullable(fromTime).map(LocalTime::parse).orElse(null);
    }

    public LocalTime getToTime() {
        return Optional.ofNullable(toTime).map(LocalTime::parse).orElse(null);
    }
}
