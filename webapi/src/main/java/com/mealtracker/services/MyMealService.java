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
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
        var existingMeal = getExistingMeal(mealId);
        if (!currentUser.isOwner(existingMeal)) {
            throw AuthorizationAppException.notResourceOwner();
        }
        input.merge(existingMeal);
        return mealRepository.save(existingMeal);
    }

    public Meal getMeal(long mealId, CurrentUser currentUser) {
        var existingMeal = getExistingMeal(mealId);
        if (!currentUser.isOwner(existingMeal)) {
            throw AuthorizationAppException.notResourceOwner();
        }
        return existingMeal;
    }

    public void deleteMeals(DeleteMyMealsInput input, CurrentUser currentUser) {
        mealRepository.deleteConsumerMeals(input.getMealIds(), currentUser.getId());
    }

    public Slice<Meal> listMeals(ListMyMealsInput input, CurrentUser currentUser) {
        var pageable = PageRequest.of(0, 2);
        return mealRepository.findByDeleted(false, pageable);
    }

    public int calculateDailyCalories(LocalDate date, CurrentUser currentUser) {
        var meals = mealRepository.findMealByConsumedDateAndConsumerAndDeleted(date, currentUser.toUser(), false);
        return meals.stream().mapToInt(Meal::getCalories).sum();
    }

    private Meal getExistingMeal(long mealId) {
        return mealRepository.findMealByIdAndDeleted(mealId, false)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.MEAL));
    }
}
