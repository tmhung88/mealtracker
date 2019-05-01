package com.mealtracker.services;

import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSettingsServiceTest {
    @InjectMocks
    private UserSettingsService userSettingsService;

    @Mock
    private UserRepository userRepository;


    @Test
    public void getUserSettings_ExistingUser_ExpectUserSettingsReturned() {
        var user = userWithSettings(415);
        when(userRepository.getOne(eq(user.getId()))).thenReturn(user);
        var userSettings = userSettingsService.getUserSettings(user.getId());

        Assertions.assertThat(userSettings).hasFieldOrPropertyWithValue("dailyCalorieLimit", 415);
    }

    private User userWithSettings(int dailyCalorieLimit) {
        var user = new User();
        user.setId(6L);

        var userSettings = new UserSettings();
        userSettings.setDailyCalorieLimit(dailyCalorieLimit);
        user.setUserSettings(userSettings);
        return user;
    }
}
