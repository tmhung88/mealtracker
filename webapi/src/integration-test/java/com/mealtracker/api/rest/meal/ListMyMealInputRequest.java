package com.mealtracker.api.rest.meal;

import lombok.Getter;

@Getter
public class ListMyMealInputRequest {
    private String fromDate;
    private String toDate;
    private String fromTime;
    private String toTime;

    public ListMyMealInputRequest fromDate(String fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public ListMyMealInputRequest toDate(String toDate) {
        this.toDate = toDate;
        return this;
    }

    public ListMyMealInputRequest fromTime(String fromTime) {
        this.fromTime = fromTime;
        return this;
    }

    public ListMyMealInputRequest toTime(String toTime) {
        this.toTime = toTime;
        return this;
    }
}
