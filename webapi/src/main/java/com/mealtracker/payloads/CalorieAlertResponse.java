package com.mealtracker.payloads;

import com.mealtracker.services.alert.CalorieAlertOutput;
import lombok.Value;

@Value
public class CalorieAlertResponse {
    private final boolean alerted;
    private final int dailyCalorieLimit;
    private final int totalCalories;

    public static SuccessEnvelop<CalorieAlertResponse> of(CalorieAlertOutput calorieAlert) {
        var response = new CalorieAlertResponse(calorieAlert.isAlerted(),
                calorieAlert.getDailyCalorieLimit(), calorieAlert.getTotalCalories());
        return new SuccessEnvelop<>(response);
    }

}
