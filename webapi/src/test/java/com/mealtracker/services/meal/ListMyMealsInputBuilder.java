package com.mealtracker.services.meal;


public class ListMyMealsInputBuilder {
    private String fromDate;
    private String toDate;
    private String fromTime;
    private String toTime;

    public ListMyMealsInputBuilder fromDate(String fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public ListMyMealsInputBuilder toDate(String toDate) {
        this.toDate = toDate;
        return this;
    }

    public ListMyMealsInputBuilder fromTime(String fromTime) {
        this.fromTime = fromTime;
        return this;
    }

    public ListMyMealsInputBuilder toTime(String toTime) {
        this.toTime = toTime;
        return this;
    }

    public ListMyMealsInput build() {
        var input = new ListMyMealsInput();
        input.setFromDate(fromDate);
        input.setToDate(toDate);
        input.setFromTime(fromTime);
        input.setToTime(toTime);
        return input;
    }
}
