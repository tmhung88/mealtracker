package com.mealtracker.api.rest;

import com.mealtracker.domains.Meal;
import com.mealtracker.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Meal addMeal(@RequestBody Meal meal) {
        return mealService.addMeal(meal);
    }
}
