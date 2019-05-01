package com.mealtracker.payloads;

import com.mealtracker.validation.LocalDateFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CalorieAlertRequest {

    @NotNull
    @LocalDateFormat
    private String date;

    public LocalDate getDate() {
        return LocalDate.parse(date);
    }

}
