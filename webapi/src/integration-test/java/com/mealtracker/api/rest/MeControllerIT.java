package com.mealtracker.api.rest;


import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.services.UserService;
import com.mealtracker.services.UserSettingsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.mealtracker.TestUser.USER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mockMvc.perform(get("/v1/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{'error':{'code':40100,'message':'No authentication token found. Please look at this document'}}"));
    }

    @Test
    public void getMySettings_ValidToken_ExpectUserSettingsReturned() throws Exception {
        var userSettings = new UserSettings();
        userSettings.setDailyCalorieLimit(500);
        when(userSettingsService.getUserSettings(USER.getId())).thenReturn(userSettings);
        mockMvc.perform(
                get("/v1/me")
                        .header("Authorization", USER.getToken())
        )
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'dailyCalorieLimit':500}}"));
    }
}
