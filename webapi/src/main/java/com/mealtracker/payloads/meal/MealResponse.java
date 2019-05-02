package com.mealtracker.payloads.meal;

import com.mealtracker.domains.Meal;
import com.mealtracker.payloads.BriefUserResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MealResponse {
    private final long id;
    private final String name;
    private final LocalDate consumedDate;
    private final LocalTime consumedTime;
    private final int calories;
    private final BriefUserResponse consumer;

    public static SuccessEnvelop<MealResponse> envelop(Meal meal) {
        var consumer = new BriefUserResponse(meal.getConsumer());
        var mealResponse = new MealResponse(meal.getId(), meal.getName(), meal.getConsumedDate(), meal.getConsumedTime(), meal.getCalories(), consumer);
        return new SuccessEnvelop<>(mealResponse);
    }
}
