package com.mealtracker.payloads.mymeal;

import com.mealtracker.domains.Meal;
import com.mealtracker.payloads.SuccessEnvelop;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class MyMealResponse {
    private final long id;
    private final String name;
    private final LocalDate consumedDate;
//
    private final LocalTime consumedTime;
    private final int calories;

    public static MyMealResponse of(Meal meal) {
        return new MyMealResponse(meal.getId(), meal.getName(), meal.getConsumedDate(), meal.getConsumedTime(), meal.getCalories());
    }
    public static SuccessEnvelop<MyMealResponse> envelop(Meal meal) {
        return new SuccessEnvelop<>(of(meal));
    }
}
