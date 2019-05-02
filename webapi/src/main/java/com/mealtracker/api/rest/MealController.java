package com.mealtracker.api.rest;

import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.meal.MealResponse;
import com.mealtracker.services.meal.MealInput;
import com.mealtracker.services.meal.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Secured("MEAL_MANAGEMENT")
@RestController
@RequestMapping("/v1/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping
    public SuccessEnvelop<MessageResponse> listMeal() {
        return MessageResponse.of("Meal listed successfully");
    }

    @PostMapping
    public SuccessEnvelop<MessageResponse> addMeal(@Valid @RequestBody MealInput mealInput) {
        mealService.addMeal(mealInput);
        return MessageResponse.of("Meal added successfully");
    }

    @DeleteMapping
    public SuccessEnvelop<MessageResponse> deleteMeals() {

        return MessageResponse.of("Meals deleted successfully");
    }

    @PutMapping("/{mealId}")
    public SuccessEnvelop<MessageResponse> updateMeal(@PathVariable long mealId,
                                                      @Valid @RequestBody MealInput mealInput) {
        mealService.updateMeal(mealId, mealInput);
        return MessageResponse.of("Meal updated successfully");
    }

    @GetMapping("/{mealId}")
    public SuccessEnvelop<MealResponse> getMeal(@PathVariable long mealId) {
        var meal = mealService.getMeal(mealId);
        return MealResponse.envelop(meal);
    }

}
