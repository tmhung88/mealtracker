package com.mealtracker.services;

import com.mealtracker.domains.Meal;
import com.mealtracker.exceptions.AuthorizationAppException;
import com.mealtracker.exceptions.ResourceName;
import com.mealtracker.exceptions.ResourceNotFoundAppException;
import com.mealtracker.repositories.MealRepository;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.mymeal.DeleteMyMealsInput;
import com.mealtracker.services.mymeal.ListMyMealsInput;
import com.mealtracker.services.mymeal.MyMealInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MyMealService {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserService userService;

    public Meal addMeal(MyMealInput input, CurrentUser currentUser) {
        var meal = input.toMeal();
        meal.setConsumer(currentUser.toUser());
        return mealRepository.save(meal);
    }

    public Meal updateMeal(long mealId, MyMealInput input, CurrentUser currentUser) {
        Meal existingMeal = getExistingMeal(mealId);
        if (!currentUser.isOwner(existingMeal)) {
            throw AuthorizationAppException.notResourceOwner();
        }
        input.merge(existingMeal);
        return mealRepository.save(existingMeal);
    }

    public Meal getMeal(long mealId, CurrentUser currentUser) {
        Meal existingMeal = getExistingMeal(mealId);
        if (!currentUser.isOwner(existingMeal)) {
            throw AuthorizationAppException.notResourceOwner();
        }
        return existingMeal;
    }

    public void deleteMeals(DeleteMyMealsInput input, CurrentUser currentUser) {
        // TODO: Check if the current user can have access to all given meals . If not, throw exceptions
        mealRepository.deleteWithIds(input.getMealIds());
    }

    public List<Meal> listMeals(ListMyMealsInput input, CurrentUser currentUser) {
        // TODO: https://dzone.com/articles/pagination-and-sorting-with-spring-data-jpa
        return new ArrayList<>();
    }


    private Meal getExistingMeal(long mealId) {
        return mealRepository.findMealByIdAndDeleted(mealId, false)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.MEAL));
    }
}
