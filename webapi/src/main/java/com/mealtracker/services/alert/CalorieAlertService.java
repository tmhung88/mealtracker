package com.mealtracker.services.alert;

import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.meal.MyMealService;
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CalorieAlertService {

    @Autowired
    private UserService userService;

    @Autowired
    private MyMealService myMealService;

    public CalorieAlertOutput getAlert(LocalDate date, CurrentUser currentUser) {
        var user = userService.getExistingUser(currentUser.getId());
        var totalCalories = myMealService.calculateDailyCalories(date, currentUser);
        var alerted = false;
        if (user.getUserSettings().isCalorieLimitEnabled()) {
            alerted = totalCalories >= user.getUserSettings().getDailyCalorieLimit();
        }
        return new CalorieAlertOutput(alerted, user.getUserSettings().getDailyCalorieLimit(), totalCalories);
    }
}
