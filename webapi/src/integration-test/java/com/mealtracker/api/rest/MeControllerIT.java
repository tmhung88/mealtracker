package com.mealtracker.api.rest;


import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.payloads.UpdateMySettingsRequest;
import com.mealtracker.services.UserSettingsService;
import com.mealtracker.services.user.UserService;
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
import static com.mealtracker.request.AppRequestBuilders.patch;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {MeController.class})
@ContextConfiguration(classes={MealTrackerApplication.class, WebSecurityConfig.class})
public class MeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSettingsService userSettingsService;

    @MockBean
    private UserService userService;

    @Test
    public void getMySettings_Anonymous_ExpectAuthenticationError() throws Exception {
        mockMvc.perform(get("/v1/users/me"))
                .andExpect(AUTHENTICATION_MISSING_TOKEN.httpStatus())
                .andExpect(AUTHENTICATION_MISSING_TOKEN.json());
    }

    @Test
    public void getMySettings_UserHasSettings_ExpectUserSettingsReturned() throws Exception {
        var userSettings = new UserSettings();
        userSettings.setDailyCalorieLimit(500);
        when(userSettingsService.getUserSettings(USER.getId())).thenReturn(userSettings);
        mockMvc.perform(get("/v1/users/me").auth(USER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'dailyCalorieLimit':500}}"));
    }

    @Test
    public void getMySettings_UserHasNoSettings_ExpectEmptyDataReturned() throws Exception {
        when(userSettingsService.getUserSettings(USER.getId())).thenReturn(null);
        mockMvc.perform(get("/v1/users/me").auth(USER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{}}"));
    }

    @Test
    public void updateMySettings_Anonymous_ExpectAuthenticationError() throws Exception {
        mockMvc.perform(patch("/v1/users/me"))
                .andExpect(AUTHENTICATION_MISSING_TOKEN.httpStatus())
                .andExpect(AUTHENTICATION_MISSING_TOKEN.json());
    }


    @Test
    public void updateMySettings_NegativeCalorieLimit_ExpectValidationError() throws Exception {
        var request = updateCalorieLimitRequest(-1);
        mockMvc.perform(patch("/v1/users/me").auth(USER).content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'dailyCalorieLimit','message':'must be greater than or equal to 0'}]}}"));
    }

    @Test
    public void updateMySettings_TooLargeCalorieLimit_ExpectValidationError() throws Exception {
        var request = updateCalorieLimitRequest(50001);
        mockMvc.perform(patch("/v1/users/me").auth(USER).content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'dailyCalorieLimit','message':'must be less than or equal to 50000'}]}}"));
    }

    @Test
    public void updateMySettings_ValidCalorieLimit_ExpectSettingsUpdated() throws Exception {
        var request = updateCalorieLimitRequest(900);
        mockMvc.perform(patch("/v1/users/me").auth(USER).content(request))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User settings updated successfully'}}"));
    }


    public UpdateMySettingsRequest updateCalorieLimitRequest(Integer calorieLimit) {
        var request = new UpdateMySettingsRequest();
        request.setDailyCalorieLimit(calorieLimit);
        return request;
    }
}
