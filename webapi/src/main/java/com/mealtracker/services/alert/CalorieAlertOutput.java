package com.mealtracker.services.alert;

import lombok.Value;

@Value
public class CalorieAlertOutput {
    private final boolean alerted;
    private final int dailyCalorieLimit;
    private final int totalCalories;
}
