package com.mealtracker.payloads;

import com.mealtracker.domains.Meal;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class MyMealResponse {
    private final long id;
    private final String name;

//    @LocalTimeFormat(iso = LocalTimeFormat.ISO.DATE)
//    @JsonSerialize(using = LocalDateSerializer.class)
    private final LocalDate consumedDate;
//
//    @LocalTimeFormat(iso = LocalTimeFormat.ISO.TIME)
//    @JsonSerialize(using = LocalTimeSerializer.class)
    private final LocalTime consumedTime;
    private final int calories;

    public static SuccessEnvelop<MyMealResponse> of(Meal meal) {
        var response = new MyMealResponse(meal.getId(), meal.getName(), meal.getConsumedDate(), meal.getConsumedTime(), meal.getCalories());
        return new SuccessEnvelop<>(response);
    }
}
