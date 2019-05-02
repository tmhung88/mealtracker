package com.mealtracker.services.meal;

import com.mealtracker.domains.Meal;
import com.mealtracker.exceptions.ResourceName;
import com.mealtracker.exceptions.ResourceNotFoundAppException;
import com.mealtracker.repositories.MealRepository;
import com.mealtracker.services.pagination.PageableBuilder;
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MealService {

    @Autowired
    private UserService userService;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PageableBuilder pageableBuilder;

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

    public void deleteMeals(DeleteMealsInput input) {
        mealRepository.deleteConsumerMeals(input.getIds());
    }

    public Meal getMeal(long mealId) {
        return getExistingMeal(mealId);
    }

    public Page<Meal> listMeals(ListMealsInput input) {
        var pageable = pageableBuilder.build(input);
        return mealRepository.findByDeleted(false, pageable);
    }

    private Meal getExistingMeal(long mealId) {
        return mealRepository.findByIdAndDeleted(mealId, false)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.MEAL));
    }
}
