package com.mealtracker.payloads.mymeal;

import com.mealtracker.domains.Meal;
import com.mealtracker.payloads.MetaSuccesEnvelop;
import com.mealtracker.payloads.PaginationMeta;
import lombok.Value;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class ListMyMealsResponse {

    private final List<MyMealResponse> meals;

    public static ListMyMealsResponse of(List<Meal> meals) {
        var myMealResponses = meals.stream().map(MyMealResponse::of).collect(Collectors.toList());
        return new ListMyMealsResponse(myMealResponses);
    }

    public static MetaSuccesEnvelop<ListMyMealsResponse, PaginationMeta> envelop(Slice<Meal> mealSlice) {
        return new MetaSuccesEnvelop<>(of(mealSlice.getContent()), PaginationMeta.of(mealSlice));
    }
}
