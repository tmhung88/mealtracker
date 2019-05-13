package com.mealtracker.services.meal;

import com.mealtracker.domains.Meal;
import com.mealtracker.exceptions.AuthorizationAppException;
import com.mealtracker.exceptions.BadRequestAppException;
import com.mealtracker.exceptions.ResourceName;
import com.mealtracker.exceptions.ResourceNotFoundAppException;
import com.mealtracker.repositories.MealRepository;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.pagination.PageableBuilder;
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class MyMealService {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PageableBuilder pageableBuilder;

    public Meal addMeal(MyMealInput input, CurrentUser currentUser) {
        var meal = input.toMeal();
        meal.setConsumer(currentUser.toUser());
        return mealRepository.save(meal);
    }

    public Meal updateMeal(long mealId, MyMealInput input, CurrentUser currentUser) {
        var existingMeal = getUserExistingMeal(mealId, currentUser.getId());
        input.merge(existingMeal);
        return mealRepository.save(existingMeal);
    }

    public Meal getMeal(long mealId, CurrentUser currentUser) {
        return getUserExistingMeal(mealId, currentUser.getId());
    }

    public void deleteMeals(DeleteMealsInput input, CurrentUser currentUser) {
        mealRepository.softDelete(input.getIds(), currentUser.getId());
    }

    public Page<Meal> listMeals(ListMyMealsInput input, CurrentUser currentUser) {
        var fromDate = input.getFromDate();
        var toDate = input.getToDate();
        boolean isValidDatePeriod = fromDate == null || toDate == null || fromDate.isBefore(toDate);
        if (!isValidDatePeriod) {
            throw BadRequestAppException.invalidDateTimeRange("fromDate", "toDate");
        }
        var fromTime = input.getFromTime();
        var toTime = input.getToTime();
        boolean isValidTimePeriod = fromTime == null || toTime == null || fromTime.isBefore(toTime);
        if (!isValidTimePeriod) {
            throw BadRequestAppException.invalidDateTimeRange("fromTime", "toTime");
        }

        var pageable = pageableBuilder.build(input);
        return mealRepository.filterMyMeals(currentUser.getId(), input.getFromDate(), input.getToDate(), input.getFromTime(), input.getToTime(), pageable);
    }

    public int calculateDailyCalories(LocalDate date, CurrentUser currentUser) {
        var meals = mealRepository.findMealByConsumedDateAndConsumerAndDeleted(date, currentUser.toUser(), false);
        return meals.stream().mapToInt(Meal::getCalories).sum();
    }

    private Meal getUserExistingMeal(long mealId, Long consumerId) {
        return mealRepository.findExistingMeal(mealId, consumerId)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.MEAL));
    }
}
