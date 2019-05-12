package com.mealtracker.api.rest.meal;

import lombok.Getter;

@Getter
public class MealRequest {

    private String name;

    private int calories;

    private String consumedDate;

    private String consumedTime;

    private long consumerId;

    public MealRequest name(String name) {
        this.name = name;
        return this;
    }

    public MealRequest calories(int calories) {
        this.calories = calories;
        return this;
    }

    public MealRequest consumedDate(String consumedDate) {
        this.consumedDate = consumedDate;
        return this;
    }

    public MealRequest consumedTime(String consumedTime) {
        this.consumedTime = consumedTime;
        return this;
    }

    public MealRequest consumerId(long consumerId) {
        this.consumerId = consumerId;
        return this;
    }
}
