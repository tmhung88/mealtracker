package com.mealtracker.api.rest;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.services.alert.CalorieAlertOutput;
import com.mealtracker.services.alert.CalorieAlertService;
import com.mealtracker.services.user.UserService;
import com.mealtracker.utils.matchers.CurrentUserMatchers;
import com.mealtracker.utils.matchers.LocalDateMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.mealtracker.TestError.AUTHENTICATION_MISSING_TOKEN;
import static com.mealtracker.TestUser.USER;
import static com.mealtracker.request.AppRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {MyAlertController.class})
@ContextConfiguration(classes = {MealTrackerApplication.class, WebSecurityConfig.class})
public class MyAlertControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalorieAlertService calorieAlertService;

    @MockBean
    private UserService userService;


    @Test
    public void getCalorieAlert_Anonymous_ExpectAuthenticationError() throws Exception {
        mockMvc.perform(get("/v1/users/me/alerts/calorie?date=2016-05-04"))
                .andExpect(AUTHENTICATION_MISSING_TOKEN.httpStatus())
                .andExpect(AUTHENTICATION_MISSING_TOKEN.json());
    }

    @Test
    public void getCalorieAlert_AuthenticatedUser_ExpectAlertDetailsReturned() throws Exception {
        var date = "2018-12-01";
        when(calorieAlertService.getAlert(LocalDateMatchers.eq(date), CurrentUserMatchers.eq(USER)))
                .thenReturn(new CalorieAlertOutput(true, 2500, 4000));

        mockMvc.perform(get("/v1/users/me/alerts/calorie?date=" + date).auth(USER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'alerted':true,'dailyCalorieLimit':2500,'totalCalories':4000}}"));
    }
}
