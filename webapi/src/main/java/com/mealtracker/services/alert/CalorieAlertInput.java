package com.mealtracker.services.alert;

import com.mealtracker.validation.LocalDateFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CalorieAlertInput {

    @NotNull
    @LocalDateFormat
    private String date;

    public LocalDate getDate() {
        return LocalDate.parse(date);
    }

}
