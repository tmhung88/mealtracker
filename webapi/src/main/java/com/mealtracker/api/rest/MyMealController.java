package com.mealtracker.api.rest;

import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.MetaSuccessEnvelop;
import com.mealtracker.payloads.PaginationMeta;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.mymeal.MyMealResponse;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.mymeal.DeleteMyMealsInput;
import com.mealtracker.services.mymeal.ListMyMealsInput;
import com.mealtracker.services.mymeal.MyMealInput;
import com.mealtracker.services.mymeal.MyMealService;
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
import java.util.List;

@RestController
@RequestMapping("/v1/users/me/meals")
@Secured("MY_MEALS")
public class MyMealController {

    @Autowired
    private MyMealService myMealService;

    @GetMapping
    public MetaSuccessEnvelop<List<MyMealResponse>, PaginationMeta> listMeal(@Valid ListMyMealsInput input,
                                                                             CurrentUser currentUser) {
        var mealPage = myMealService.listMeals(input, currentUser);
        return MyMealResponse.envelop(mealPage);
    }

    @PostMapping
    public SuccessEnvelop<MessageResponse> addMeal(@Valid @RequestBody MyMealInput request, CurrentUser currentUser) {
        myMealService.addMeal(request, currentUser);
        return MessageResponse.of("Meal added successfully");
    }

    @DeleteMapping
    public SuccessEnvelop<MessageResponse> deleteMeals(@Valid @RequestBody DeleteMyMealsInput request, CurrentUser currentUser) {
        myMealService.deleteMeals(request, currentUser);
        return MessageResponse.of("Meals deleted successfully");
    }

    @PutMapping("/{mealId}")
    public SuccessEnvelop<MessageResponse> updateMeal(@PathVariable long mealId,
                                                      @Valid @RequestBody MyMealInput request,
                                                      CurrentUser currentUser) {
        myMealService.updateMeal(mealId, request, currentUser);
        return MessageResponse.of("Meal updated successfully");
    }

    @GetMapping("/{mealId}")
    public SuccessEnvelop<MyMealResponse> getMeal(@PathVariable long mealId,
                                                  CurrentUser currentUser) {
        var meal = myMealService.getMeal(mealId, currentUser);
        return MyMealResponse.envelop(meal);
    }


}
