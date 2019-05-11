package com.mealtracker.payloads.meal;

import com.mealtracker.domains.Meal;
import com.mealtracker.payloads.MetaSuccessEnvelop;
import com.mealtracker.payloads.PaginationMeta;
import com.mealtracker.payloads.SuccessEnvelop;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MealResponse {
    private final long id;
    private final String name;
    private final LocalDate consumedDate;
    private final LocalTime consumedTime;
    private final int calories;
    private final BriefUserResponse consumer;

    private MealResponse(Meal meal) {
        id = meal.getId();
        name = meal.getName();
        consumedDate = meal.getConsumedDate();
        consumedTime = meal.getConsumedTime();
        calories = meal.getCalories();
        consumer = new BriefUserResponse(meal.getConsumer());
    }

    public static SuccessEnvelop<MealResponse> envelop(Meal meal) {
        return new SuccessEnvelop<>(new MealResponse(meal));
    }

    public static MetaSuccessEnvelop<List<MealResponse>, PaginationMeta> envelop(Page<Meal> mealPage) {
        var myMealResponses = mealPage.getContent().stream().map(MealResponse::new).collect(Collectors.toList());
        return new MetaSuccessEnvelop<>(myMealResponses, PaginationMeta.of(mealPage));
    }
}
