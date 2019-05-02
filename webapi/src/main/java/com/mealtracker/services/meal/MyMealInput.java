package com.mealtracker.services.meal;

import com.mealtracker.domains.Meal;
import com.mealtracker.validation.LocalDateFormat;
import com.mealtracker.validation.LocalTimeFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MyMealInput {

    @Size(min = 2, max = 200)
    private String name;

    @Min(1)
    @Max(10000)
    private int calories;

    @NotNull
    @LocalDateFormat
    private String consumedDate;

    @NotNull
    @LocalTimeFormat
    private String consumedTime;

    public Meal toMeal() {
        var meal = new Meal();
        meal.setName(name);
        meal.setCalories(calories);
        meal.setConsumedDate(getConsumedDate());
        meal.setConsumedTime(getConsumedTime());
        return meal;
    }

    public void merge(Meal existingMeal) {
        existingMeal.setName(name);
        existingMeal.setCalories(calories);
        existingMeal.setConsumedDate(getConsumedDate());
        existingMeal.setConsumedTime(getConsumedTime());
    }

    public LocalDate getConsumedDate() {
        return LocalDate.parse(consumedDate);
    }

    public LocalTime getConsumedTime() {
        return LocalTime.parse(consumedTime);
    }

}
