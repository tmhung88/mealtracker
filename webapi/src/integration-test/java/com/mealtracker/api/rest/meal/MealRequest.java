package com.mealtracker.api.rest.meal;

import lombok.Getter;

@Getter
class MealRequest {

    private String name;

    private int calories;

    private String consumedDate;

    private String consumedTime;

    private long consumerId;

    MealRequest name(String name) {
        this.name = name;
        return this;
    }

    MealRequest calories(int calories) {
        this.calories = calories;
        return this;
    }

    MealRequest consumedDate(String consumedDate) {
        this.consumedDate = consumedDate;
        return this;
    }

    MealRequest consumedTime(String consumedTime) {
        this.consumedTime = consumedTime;
        return this;
    }

    MealRequest consumerId(long consumerId) {
        this.consumerId = consumerId;
        return this;
    }
}
