package com.mealtracker.domains;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class UserSettings {
    private static final int DISABLED_CALORIE_LIMIT = 0;

    @Column(name = "daily_calorie_limit", nullable = false)
    private int dailyCalorieLimit = DISABLED_CALORIE_LIMIT;

    public boolean isCalorieLimitEnabled() {
        return dailyCalorieLimit > DISABLED_CALORIE_LIMIT;
    }
}
