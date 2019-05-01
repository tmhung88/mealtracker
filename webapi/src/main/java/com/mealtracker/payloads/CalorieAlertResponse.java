package com.mealtracker.payloads;

import lombok.Value;

@Value
public class CalorieAlertResponse {
    private final boolean alerted;
    private final int dailyCalorieLimit;
    private final int totalCalories;

    public static SuccessEnvelop<CalorieAlertResponse> of() {
        return new SuccessEnvelop<>(new CalorieAlertResponse(false, 0, 5000));
    }
}
