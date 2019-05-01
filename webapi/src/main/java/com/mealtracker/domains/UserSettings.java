package com.mealtracker.domains;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class UserSettings {

    @Column(name = "daily_calorie_limit", nullable = false)
    private int dailyCalorieLimit = 0;
}
