package com.mealtracker.api.rest;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.services.meal.MealService;
import com.mealtracker.services.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.mealtracker.TestError.AUTHORIZATION_API_ACCESS_DENIED;
import static com.mealtracker.TestUser.NO_MEAL_MANAGEMENT;
import static com.mealtracker.request.AppRequestBuilders.delete;
import static com.mealtracker.request.AppRequestBuilders.get;
import static com.mealtracker.request.AppRequestBuilders.post;
import static com.mealtracker.request.AppRequestBuilders.put;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {MealController.class})
@ContextConfiguration(classes={MealTrackerApplication.class, WebSecurityConfig.class})
public class MealControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @MockBean
    private UserService userService;

    @Test
    public void listMeal_NoMealManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(get("/v1/meals").auth(NO_MEAL_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void addMeal_NoMealManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(post("/v1/meals").auth(NO_MEAL_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void deleteMeals_NoMealManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(delete("/v1/meals").auth(NO_MEAL_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void updateMeal_NoMealManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(put("/v1/meals/10").auth(NO_MEAL_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void getMeal_NoMealManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(get("/v1/meals/52").auth(NO_MEAL_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }
}
