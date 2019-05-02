package com.mealtracker.api.rest.meal;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.api.rest.MyMealController;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.Meal;
import com.mealtracker.services.meal.DeleteMealsInput;
import com.mealtracker.services.meal.MyMealService;
import com.mealtracker.services.user.UserService;
import com.mealtracker.utils.matchers.CurrentUserMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static com.mealtracker.TestError.AUTHORIZATION_API_ACCESS_DENIED;
import static com.mealtracker.TestUser.NO_MY_MEAL;
import static com.mealtracker.TestUser.USER;
import static com.mealtracker.request.AppRequestBuilders.delete;
import static com.mealtracker.request.AppRequestBuilders.get;
import static com.mealtracker.request.AppRequestBuilders.post;
import static com.mealtracker.request.AppRequestBuilders.put;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {MyMealController.class})
@ContextConfiguration(classes={MealTrackerApplication.class, WebSecurityConfig.class})
public class MyMealControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MyMealService myMealService;

    @Test
    public void addMeal_InvalidName_ExpectBadInput() throws Exception {
        mockMvc.perform(
                post("/v1/users/me/meals").auth(USER).content(addMealRequest().name("a")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'name','message':'size must be between 2 and 200'}]}}"));
    }

    @Test
    public void addMeal_TooSmallCalories_ExpectBadInput() throws Exception {
        mockMvc.perform(
                post("/v1/users/me/meals").auth(USER).content(addMealRequest().calories(0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'calories','message':'must be greater than or equal to 1'}]}}"));
    }

    @Test
    public void addMeal_TooLargeCalories_ExpectBadInput() throws Exception {
        mockMvc.perform(
                post("/v1/users/me/meals").auth(USER).content(addMealRequest().calories(10001)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'calories','message':'must be less than or equal to 10000'}]}}"));
    }

    @Test
    public void addMeal_InvalidConsumedDate_ExpectBadInput() throws Exception {
        mockMvc.perform(
                post("/v1/users/me/meals").auth(USER).content(addMealRequest().consumedDate("2019/11/01")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'consumedDate','message':'Date should be in this format yyyy-MM-dd'}]}}"));
    }

    @Test
    public void addMeal_InvalidConsumedTime_ExpectBadInput() throws Exception {

        mockMvc.perform(
                post("/v1/users/me/meals").auth(USER).content(addMealRequest().consumedTime("20.05")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'consumedTime','message':'Time should be in this format hh:mm'}]}}"));
    }

    @Test
    public void addMeal_NoMyMealUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(
                post("/v1/users/me/meals").auth(NO_MY_MEAL).content(addMealRequest()))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void addMeal_ValidAddMealRequest_ExpectMealAdded() throws Exception {

        mockMvc.perform(
                post("/v1/users/me/meals").auth(USER).content(addMealRequest()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Meal added successfully'}}"));
    }

    @Test
    public void updateMeal_NoMyMealUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(
                put("/v1/users/me/meals/5").auth(NO_MY_MEAL).content(updateMealRequest()))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void updateMeal_ValidUpdateMealRequest_ExpectMealUpdated() throws Exception {
        mockMvc.perform(
                put("/v1/users/me/meals/5").auth(USER).content(updateMealRequest()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Meal updated successfully'}}"));
    }

    @Test
    public void getMeal_NoMyMealUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(
                get("/v1/users/me/meals/155").auth(NO_MY_MEAL))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void getMeal_MyMealUser_ExpectMealReturned() throws Exception {
        when(myMealService.getMeal(eq(4L), CurrentUserMatchers.eq(USER))).thenReturn(completeMealDetails());
        mockMvc.perform(
                get("/v1/users/me/meals/4").auth(USER).content(updateMealRequest()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'id':9,'name':'Coffee','consumedDate':'2011-05-08','consumedTime':'05:05:00','calories':100}}"));
    }

    @Test
    public void deleteMeals_NoMyMealUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(
                delete("/v1/users/me/meals").auth(NO_MY_MEAL).content(deleteMyMealsRequest(5L, 6L)))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void deleteMeals_MyMealUser_NoIds_ExpectBadInputError() throws Exception {
        mockMvc.perform(
                delete("/v1/users/me/meals").auth(USER).content(deleteMyMealsRequest()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'ids','message':'size must be between 1 and 50'}]}}"));
    }

    @Test
    public void deleteMeals_MyMealUser_SomeIds_ExpectMealsDeleted() throws Exception {
        mockMvc.perform(
                delete("/v1/users/me/meals").auth(USER).content(deleteMyMealsRequest(1L, 5L)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Meals deleted successfully'}}"));
    }


    private MealRequest addMealRequest() {
        return new MealRequest().calories(500).consumedDate("2019-05-01").consumedTime("02:25").name("Pizza");
    }

    private MealRequest updateMealRequest() {
        return new MealRequest().calories(400).consumedDate("2016-02-09").consumedTime("14:10").name("Soup");
    }

    private DeleteMealsInput deleteMyMealsRequest(Long... ids) {
        var input = new DeleteMealsInput();
        input.setIds(Arrays.asList(ids));
        return input;
    }

    private Meal completeMealDetails() {
        Meal meal = new Meal();
        meal.setId(9L);
        meal.setConsumedDate(LocalDate.of(2011, 5, 8));
        meal.setConsumedTime(LocalTime.of(5, 5));
        meal.setCalories(100);
        meal.setName("Coffee");
        return meal;
    }
}
