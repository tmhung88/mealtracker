package com.mealtracker.services.alert;

import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.meal.MyMealService;
import com.mealtracker.services.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static com.mealtracker.utils.matchers.CommonMatchers.eq;
import static com.mealtracker.utils.matchers.CommonMatchers.fields;
import static com.mealtracker.utils.matchers.LocalDateMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalorieAlertServiceTest {

    private static final int DISABLED_CALORIE_LIMIT = 0;

    @InjectMocks
    private CalorieAlertService calorieAlertService;

    @Mock
    private UserService userService;

    @Mock
    private MyMealService myMealService;


    @Test
    public void getAlert_DailyLimitDisabled_ExpectNoAlert() {
        var currentUser = currentUser(4L);
        var anyConsumptionAmount = 59355;
        var user = user(DISABLED_CALORIE_LIMIT);
        when(userService.getExistingUser(currentUser.getId())).thenReturn(user);
        when(myMealService.calculateDailyCalories(eq("2000-01-01"), eq(fields().id(4L))))
                .thenReturn(anyConsumptionAmount);

        var output = calorieAlertService.getAlert(LocalDate.of(2000, 1, 1), currentUser);
        Assertions.assertThat(output).hasFieldOrPropertyWithValue("alerted", false)
                .hasFieldOrPropertyWithValue("dailyCalorieLimit", user.getUserSettings().getDailyCalorieLimit())
                .hasFieldOrPropertyWithValue("totalCalories", anyConsumptionAmount);
    }

    @Test
    public void getAlert_ConsumptionLessThanLimit_ExpectNoAlert() {
        var currentUser = currentUser(13L);
        var acceptableConsumption = 499;
        var user = user(500);
        when(userService.getExistingUser(currentUser.getId())).thenReturn(user);
        when(myMealService.calculateDailyCalories(eq("2012-12-12"), eq(fields().id(13L))))
                .thenReturn(acceptableConsumption);

        var output = calorieAlertService.getAlert(LocalDate.of(2012, 12, 12), currentUser);
        Assertions.assertThat(output).hasFieldOrPropertyWithValue("alerted", false)
                .hasFieldOrPropertyWithValue("dailyCalorieLimit", user.getUserSettings().getDailyCalorieLimit())
                .hasFieldOrPropertyWithValue("totalCalories", acceptableConsumption);
    }

    @Test
    public void getAlert_ConsumptionExceedLimit_ExpectAlert() {
        var currentUser = currentUser(24L);
        var acceptableConsumption = 100;
        var user = user(100);
        when(userService.getExistingUser(currentUser.getId())).thenReturn(user);
        when(myMealService.calculateDailyCalories(eq("2019-05-05"), eq(fields().id(24L))))
                .thenReturn(acceptableConsumption);

        var output = calorieAlertService.getAlert(LocalDate.of(2019, 5, 5), currentUser);
        Assertions.assertThat(output).hasFieldOrPropertyWithValue("alerted", true)
                .hasFieldOrPropertyWithValue("dailyCalorieLimit", user.getUserSettings().getDailyCalorieLimit())
                .hasFieldOrPropertyWithValue("totalCalories", acceptableConsumption);
    }


    CurrentUser currentUser(long id) {
        var currentUser = Mockito.mock(CurrentUser.class);
        when(currentUser.getId()).thenReturn(id);
        return currentUser;
    }

    User user(int dailyCalorieLimit) {
        var settings = new UserSettings();
        settings.setDailyCalorieLimit(dailyCalorieLimit);

        var user = new User();
        user.setUserSettings(settings);
        return user;
    }
}
