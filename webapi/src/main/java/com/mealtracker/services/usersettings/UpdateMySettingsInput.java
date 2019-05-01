package com.mealtracker.services.usersettings;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class UpdateMySettingsInput {

    @Min(0)
    @Max(50000)
    private Integer dailyCalorieLimit;

    public boolean isDailyCalorieLimitPatched() {
        return dailyCalorieLimit != null;
    }
}
