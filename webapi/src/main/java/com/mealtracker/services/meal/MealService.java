package com.mealtracker.services.meal;

import com.mealtracker.domains.Meal;
import com.mealtracker.exceptions.ResourceName;
import com.mealtracker.exceptions.ResourceNotFoundAppException;
import com.mealtracker.repositories.MealRepository;
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MealService {

    @Autowired
    private UserService userService;

    @Autowired
    private MealRepository mealRepository;

    public Meal addMeal(MealInput input) {
        userService.getExistingUser(input.getConsumerId());
        var meal = input.toMeal();
        return mealRepository.save(meal);
    }

    public Meal updateMeal(long mealId, MealInput input) {
        userService.getExistingUser(input.getConsumerId());
        var existingMeal = getExistingMeal(mealId);
        input.merge(existingMeal);
        return mealRepository.save(existingMeal);
    }

    public Meal getMeal(long mealId) {
        return getExistingMeal(mealId);
    }

    private Meal getExistingMeal(long mealId) {
        return mealRepository.findMealByIdAndDeleted(mealId, false)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.MEAL));
    }


}
